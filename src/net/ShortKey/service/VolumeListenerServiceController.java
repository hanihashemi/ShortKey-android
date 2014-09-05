package net.ShortKey.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.settings.SettingsProperty;

/**
 * Created by hani on 7/30/14.
 */
public class VolumeListenerServiceController {
    public void restart() {
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

    public void setMusicStatus(final int value) {

        ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                Messenger mService = new Messenger(service);
                try {
                    Message msg = Message.obtain(null, ShortKeyService.MSG_SET_STRING_VALUE, value, 0);
                    mService.send(msg);
                } catch (RemoteException e) {
                    Log.d("ShortKey Error: ", e.getMessage());
                } finally {
                    ApplicationContextProvider.getContext().unbindService(this);
                }
            }

            public void onServiceDisconnected(ComponentName className) {
            }
        };

        ApplicationContextProvider.getContext().bindService(new Intent(ApplicationContextProvider.getContext(), ShortKeyService.class), mConnection, Context.BIND_AUTO_CREATE);
    }
}
