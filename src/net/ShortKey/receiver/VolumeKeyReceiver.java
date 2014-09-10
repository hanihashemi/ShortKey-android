package net.ShortKey.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.R;
import net.ShortKey.service.ShortKeyServiceController;
import net.ShortKey.settings.SettingsProperty;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hani on 7/19/14.
 */
public class VolumeKeyReceiver extends BroadcastReceiver {
    AudioManager mAudioManager;
    private ChangeSongRunnable changeSongRunnable;
    private Context mContext;
    private Handler mHandler;
    private int mWaitTime;

    private int lastVolume;
    private Timer timer;

    public enum ActionType {Up, Down, DoubleUp, DoubleDown}

    ;

    public enum Mode {Normal}

    ;


    public VolumeKeyReceiver(final Context mContext) {
        super();
        mWaitTime = 500;
        mHandler = new Handler();
        lastVolume = new SettingsProperty().getDefaultVolume();
        this.mContext = mContext;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, 0);
    }

    private static void sendMediaKeyCode(final Context context, final int n) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON", null);
        intent.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(uptimeMillis, uptimeMillis, 0, n, 0));
        context.sendBroadcast(intent);
        final Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON", null);
        intent2.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(uptimeMillis, uptimeMillis, 1, n, 0));
        context.sendBroadcast(intent2);
    }

    private void volumeChangedOnMusicMode() {
        final int streamMusicVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final int delta = streamMusicVolume - lastVolume;

        lastVolume = new SettingsProperty().getDefaultVolume();
        if (delta != 0)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, 0);
        if ((delta == -1 || delta == 1)) {
            if (delta == 1) {
                runAction(ActionType.Up);
            } else {
                runAction(ActionType.Down);
            }
        }
    }

    public void onReceive(final Context context, final Intent intent) {
        if (mAudioManager.getMode() != 0)
            return;

        if (mAudioManager.isMusicActive())
            if (new SettingsProperty().getCheckboxEnableWhenScreenIsOff()) {
                PowerManager pm = (PowerManager) ApplicationContextProvider.getContext().getSystemService(Context.POWER_SERVICE);
                if (pm.isScreenOn())
                    return;
            }
        volumeChangedOnMusicMode();
    }

    private void runAction(ActionType actionCode) {
        int timerDelay = 500;

        if (changeSongRunnable != null)
            changeSongRunnable.cancel();

        if (timer != null) {
            timer.cancel();
            timerDelay = 1;

            if (actionCode == ActionType.Up)
                actionCode = ActionType.DoubleUp;
            else if (actionCode == ActionType.Down)
                actionCode = ActionType.DoubleDown;
            else Log.d("VolumeReceiver", "Bad Action (12)");
        }

        timer = new Timer();
        timer.schedule(new ActionTimer(actionCode), timerDelay);
    }

    private class ActionTimer extends TimerTask {

        private ActionType actionType;

        public ActionTimer(ActionType actionType) {
            this.actionType = actionType;
        }

        @Override
        public void run() {
            timer.cancel();
            timer = null;

            changeSongRunnable = new ChangeSongRunnable(actionType, Mode.Normal);
            mHandler.postDelayed(changeSongRunnable, (long) mWaitTime);
        }
    }

    private class ChangeSongRunnable implements Runnable {
        private ActionType actionType;
        private boolean canceled = false;
        private Mode mode;

        public ChangeSongRunnable(final ActionType actionType, Mode mode) {
            this.actionType = actionType;
            this.mode = mode;
        }

        public void cancel() {
            canceled = true;
        }

        public void run() {
            if (!this.canceled)
                switch (actionType) {
                    case Up: {
                        if (mode == Mode.Normal)
                            doActionOnNormalMode(new SettingsProperty().getUpAction());
                        break;
                    }
                    case Down: {
                        if (mode == Mode.Normal)
                            doActionOnNormalMode(new SettingsProperty().getDownAction());
                        break;
                    }
                    case DoubleUp: {
                        if (mode == Mode.Normal)
                            doActionOnNormalMode(new SettingsProperty().getDoubleUpAction());
                        break;
                    }
                    case DoubleDown: {
                        if (mode == Mode.Normal)
                            doActionOnNormalMode(new SettingsProperty().getDoubleDownAction());
                        break;
                    }
                }
        }

        private void doActionOnNormalMode(String action) {
            if (action.equals(ApplicationContextProvider.getContext().getString(R.string.value_of_action_next_track))) {
                sendMediaKeyCode(mContext, KeyEvent.KEYCODE_MEDIA_NEXT);
                MediaPlayer.create(ApplicationContextProvider.getContext(), R.raw.next).start();
            } else if (action.equals(ApplicationContextProvider.getContext().getString(R.string.value_of_action_previous_track))) {
                sendMediaKeyCode(mContext, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                MediaPlayer.create(ApplicationContextProvider.getContext(), R.raw.previous).start();
            } else if (action.equals(ApplicationContextProvider.getContext().getString(R.string.value_of_action_play_track))) {
                sendMediaKeyCode(mContext, KeyEvent.KEYCODE_MEDIA_PLAY);
                MediaPlayer.create(ApplicationContextProvider.getContext(), R.raw.play).start();
            } else if (action.equals(ApplicationContextProvider.getContext().getString(R.string.value_of_action_pause_track))) {
                sendMediaKeyCode(mContext, KeyEvent.KEYCODE_MEDIA_PAUSE);
                MediaPlayer.create(ApplicationContextProvider.getContext(), R.raw.pause).start();
            }else if (action.equals(ApplicationContextProvider.getContext().getString(R.string.value_of_action_start_video_recorder))) {
                new ShortKeyServiceController().sendMessage(ShortKeyServiceController.MSG_START_RECORD);
            }else if (action.equals(ApplicationContextProvider.getContext().getString(R.string.value_of_action_stop_video_recorder))) {
                new ShortKeyServiceController().sendMessage(ShortKeyServiceController.MSG_STOP_RECORD);
            }
        }
    }
}