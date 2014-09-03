package net.ShortKey.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
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


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Notification().show();
        registerVolumeKeyReceiver();
        registerScreenReceiver();

        setPlaying();

        return Service.START_NOT_STICKY;
    }

    private void setPlaying() {
        if (new SettingsProperty().getCheckboxEnableWhenScreenIsOff()) {
            PowerManager pm = (PowerManager) ApplicationContextProvider.getContext().getSystemService(Context.POWER_SERVICE);
            if (pm.isScreenOn())
                return;
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.empty);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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
        if (mediaPlayer != null)
            mediaPlayer.stop();

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
}
