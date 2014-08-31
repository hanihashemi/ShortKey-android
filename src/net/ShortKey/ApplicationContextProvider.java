package net.ShortKey;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import net.ShortKey.service.ShortKeyService;

/**
 * Created by hani on 7/19/14.
 */
public class ApplicationContextProvider extends Application {

    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * check is service SCheckServer running.
     *
     * @return boolean
     */
    public static boolean isSCheckServerRunning() {
        try {
            ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (ShortKeyService.class.getName().equals(service.service.getClassName()))
                    return true;
            }
        } catch (NullPointerException e) {
        }
        return false;
    }
}

