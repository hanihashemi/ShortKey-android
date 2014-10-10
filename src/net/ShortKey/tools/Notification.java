package net.ShortKey.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.Main;
import net.ShortKey.R;

/**
 * Created by hani on 7/31/14.
 */
public class Notification {
    public static int NOTIFICATION_ID = 1161065;

    public enum Type {Normal, Recording}

    public void show(Service service, Type type) {
        if (type.equals(Type.Normal))
            createNotification(service, ApplicationContextProvider.getContext().getString(R.string.notification_summary), R.drawable.app_logo_notification);
        else
            createNotification(service, ApplicationContextProvider.getContext().getString(R.string.notification_recording_summary), R.drawable.recording_notification);
    }

    public static void close() {
        NotificationManager mNotificationManager = (NotificationManager) ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * @param service
     * @param message
     * @param icon
     */
    public void createNotification(Service service, String message, int icon) {
        android.app.Notification note = new android.app.Notification(
                icon,
                message,
                System.currentTimeMillis()
        );

        Intent i = new Intent(ApplicationContextProvider.getContext(), Main.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(ApplicationContextProvider.getContext(), 0,
                i, 0);
        note.setLatestEventInfo(ApplicationContextProvider.getContext(), ApplicationContextProvider.getContext().getString(R.string.app_name),
                message,
                pi);
        note.flags |= android.app.Notification.FLAG_NO_CLEAR;
        service.startForeground(1337, note);
    }
}