package net.ShortKey.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import net.ShortKey.service.ShortKeyServiceController;
import net.ShortKey.settings.SettingsProperty;

/**
 * Created by hani on 9/1/14.
 */
public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (new SettingsProperty().getCheckboxEnableWhenMusicIsPlay())
                return;

            if (new SettingsProperty().getCheckboxEnableWhenScreenIsOff())
                new ShortKeyServiceController().sendMessage(ShortKeyServiceController.MSG_PLAY_MUSIC);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (new SettingsProperty().getCheckboxEnableWhenMusicIsPlay())
                return;

            if (new SettingsProperty().getCheckboxEnableWhenScreenIsOff())
                new ShortKeyServiceController().sendMessage(ShortKeyServiceController.MSG_STOP_MUSIC);
        }
    }

}
