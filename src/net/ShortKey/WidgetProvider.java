package net.ShortKey;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import net.ShortKey.service.ShortKeyServiceController;

/**
 * Created by hani on 10/12/14.
 */

public class WidgetProvider extends AppWidgetProvider {
    public static String WIDGET_BUTTON = "net.ShortKey.layout.widget.widgetIcon";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widgetIcon, getPendingSelfIntent(context, WIDGET_BUTTON));

            if (ApplicationContextProvider.isSCheckServerRunning()) {
                serviceIsRunning(context);
            } else {
                serviceIsStop(context);
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WIDGET_BUTTON.equals(intent.getAction())) {
            new ShortKeyServiceController().ToggleServiceStatusAndSetCheckbox();
        }
    }

    private void serviceIsRunning(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews;
        ComponentName watchWidget;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        watchWidget = new ComponentName(context, WidgetProvider.class);

        remoteViews.setTextViewText(R.id.widgetText, "فعال");
        remoteViews.setImageViewResource(R.id.widgetIcon, R.drawable.widget_logo);

        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    private void serviceIsStop(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews;
        ComponentName watchWidget;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        watchWidget = new ComponentName(context, WidgetProvider.class);

        remoteViews.setTextViewText(R.id.widgetText, "غیرفعال");
        remoteViews.setImageViewResource(R.id.widgetIcon, R.drawable.widget_logo_grayscale);

        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    /**
     * @param context
     * @param action
     * @return
     */
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}