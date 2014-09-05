package net.ShortKey.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.*;
import android.util.Log;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.R;
import net.ShortKey.ScreenReceiver;
import net.ShortKey.VolumeKeyReceiver;
import net.ShortKey.notify.Notification;
import net.ShortKey.settings.SettingsProperty;

/**
 * Created by hani on 7/19/14.
 */
public class ShortKeyService extends Service {
    private VolumeKeyReceiver volumeKeyReceiver = null;
    private ScreenReceiver screenReceiver = null;
    private static MediaPlayer mediaPlayer;

    static final int MSG_SET_STRING_VALUE = 4;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_STRING_VALUE:
                    if (msg.arg1 == 1)
                        playMusic();
                    else
                        stopMusic();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Notification().show();
        registerVolumeKeyReceiver();
        registerScreenReceiver();

        setMusicStatus();

        return Service.START_NOT_STICKY;
    }

    private void setMusicStatus() {
        if (new SettingsProperty().getCheckboxEnableWhenScreenIsOff()) {
            PowerManager pm = (PowerManager) ApplicationContextProvider.getContext().getSystemService(Context.POWER_SERVICE);
            if (pm.isScreenOn())
                return;
        }
        playMusic();
    }

    public void onCreate() {
        super.onCreate();
    }

    private void registerVolumeKeyReceiver() {
        if (volumeKeyReceiver == null) {
            volumeKeyReceiver = new VolumeKeyReceiver(this.getApplicationContext());

            final IntentFilter intentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
            intentFilter.setPriority(Integer.MAX_VALUE);
            intentFilter.addCategory("android.intent.category.DEFAULT");
            this.getApplicationContext().registerReceiver(volumeKeyReceiver, intentFilter);
        }
    }

    private void registerScreenReceiver() {
        if (screenReceiver == null) {
            screenReceiver = new ScreenReceiver();

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(screenReceiver, filter);
        }
    }

    @TargetApi(14)
    private void unregisterVolumeKeyReceiver() {
        if (volumeKeyReceiver != null) {
            try {
                unregisterReceiver(volumeKeyReceiver);
            } catch (Exception ex) {
                Log.d("Volume service", ex.getMessage());
            }
        }
    }

    @TargetApi(14)
    private void unregisterScreenReceiver() {
        if (screenReceiver != null) {
            try {
                unregisterReceiver(screenReceiver);
            } catch (Exception ex) {
                Log.d("screen service", ex.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        restartService();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        restartService();
    }

    private void restartService() {
        stopMusic();

        unregisterScreenReceiver();
        unregisterVolumeKeyReceiver();

        Notification.close();

        if (new SettingsProperty().getCheckboxService()) {
            Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
            restartServiceIntent.setPackage(getPackageName());

            PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 500,
                    restartServicePendingIntent);
        }
    }

    private void playMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.empty);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        Log.d("=====", "play music");
    }

    private void stopMusic() {
        if (mediaPlayer != null)
            mediaPlayer.stop();

        Log.d("=====", "stop music");
    }
}
