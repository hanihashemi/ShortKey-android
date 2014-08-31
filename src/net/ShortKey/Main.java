package net.ShortKey;

import android.app.Activity;
import android.os.Bundle;
import net.ShortKey.service.VolumeListenerServiceController;
import net.ShortKey.settings.SettingsFragment;

public class Main extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MultiLanguage().setLanguage();

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();

        new VolumeListenerServiceController().ToggleServiceStatus();
    }
}
