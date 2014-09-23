package net.ShortKey.admin;

/**
 * Created by hani on 9/17/14.
 */

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.R;

public class DarPolicy extends DeviceAdminReceiver {
    static SharedPreferences getSamplePreferences(Context context) {
        return context.getSharedPreferences(
                DeviceAdminReceiver.class.getName(), 0);
    }

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, ApplicationContextProvider.getContext().getString(R.string.admin_access_enabled));
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This is an optional message to warn the user about disabling.";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, ApplicationContextProvider.getContext().getString(R.string.admin_access_disabled));
    }
}

