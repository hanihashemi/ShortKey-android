package net.ShortKey.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.Main;
import net.ShortKey.R;

/**
 * Created by hani on 7/31/14.
 */
public class Notification {
    public static int NOTIFICATION_ID = 1161065;

    public void show() {
        this.createNotification(ApplicationContextProvider.getContext().getString(R.string.notification_summary));
    }

    private void createNotification(String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) ApplicationContextProvider.getContext().getSystemService
                        (ApplicationContextProvider.getContext().NOTIFICATION_SERVICE);
        android.app.Notification notification = new android.app.Notification(R.drawable.app_logo_notification, message, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationContextProvider.getContext(), 0, new Intent(ApplicationContextProvider.getContext(), Main
                .class), 0);
        notification.setLatestEventInfo(ApplicationContextProvider.getContext(), ApplicationContextProvider.getContext().getString(R.string.app_name), message, contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void AlertNotify() {
        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ApplicationContextProvider.getContext(), sound);
            r.play();
        } catch (Exception e) {
        }
    }

    public static void close() {
        NotificationManager mNotificationManager = (NotificationManager) ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}
