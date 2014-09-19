package net.ShortKey.admin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.preference.PreferenceFragment;
import net.ShortKey.ApplicationContextProvider;

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
        }
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
                "Additional text explaining why this needs to be added.");
        preferenceFragment.startActivityForResult(intent, 1);
    }

    public void deactiveAdminMode() {
        deviceManger.removeActiveAdmin(compName);
    }
}
