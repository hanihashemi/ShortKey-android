package net.ShortKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hani on 9/1/14.
 */
public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d("=======" , "OFF");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("=======", "ON");
        }
    }

}
