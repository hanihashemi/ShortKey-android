package net.ShortKey.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.settings.SettingsProperty;

/**
 * Created by hani on 7/30/14.
 */
public class VolumeListenerServiceController {
    public void restart()
    {
        ApplicationContextProvider.getContext().stopService(new Intent(ApplicationContextProvider.getContext(), ShortKeyService.class));
    }

    public void ToggleServiceStatus() {
        if (new SettingsProperty().getCheckboxService()) {
            if (!ApplicationContextProvider.isSCheckServerRunning()) {
                ApplicationContextProvider.getContext().startService(new Intent(ApplicationContextProvider.getContext(), ShortKeyService.class));
            }
        } else {
            ApplicationContextProvider.getContext().stopService(new Intent(ApplicationContextProvider.getContext(), ShortKeyService.class));
        }
    }
}
