package net.ShortKey.admin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.R;

/**
 * Created by hani on 9/17/14.
 */
public class AdminController {

    private ComponentName compName;
    private DevicePolicyManager deviceManger;

    public AdminController() {
        compName = new ComponentName(ApplicationContextProvider.getContext(), DarPolicy.class);
        deviceManger = (DevicePolicyManager) ApplicationContextProvider.getContext().getSystemService(
                Context.DEVICE_POLICY_SERVICE);
    }

    public void LockScreen() {
        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            deviceManger.lockNow();
        }else
            Toast.makeText(ApplicationContextProvider.getContext(), ApplicationContextProvider.getContext().getString(R.string.admin_toast_alert) , Toast.LENGTH_LONG).show();
    }

    public void UnLockScreen() {
        PowerManager pm = (PowerManager) ApplicationContextProvider.getContext()
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
    }

    public boolean isActivate() {
        return deviceManger.isAdminActive(compName);
    }

    public void activeAdminMode(PreferenceFragment preferenceFragment) {
        Intent intent = new Intent(DevicePolicyManager
                .ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                ApplicationContextProvider.getContext().getString(R.string.admin_access_description_in_dialog_box));
        preferenceFragment.startActivityForResult(intent, 1);
    }

    public void deactiveAdminMode() {
        deviceManger.removeActiveAdmin(compName);
    }
}
