package net.ShortKey;

import android.content.res.Configuration;
import net.ShortKey.settings.SettingsProperty;

import java.util.Locale;

/**
 * Created by hani on 8/23/14.
 */
public class MultiLanguage {
    public Configuration setLanguage() {
        if (new SettingsProperty().getLanguage().equals(ApplicationContextProvider.getContext().getString(R.string.value_of_language_persian))) {
            return setFarsi();
        } else {
            return setEnglish();
        }
    }

    private Configuration setFarsi() {
        Locale locale = new Locale("fa_IR");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ApplicationContextProvider.getContext().getResources().updateConfiguration(config, null);

        return config;
    }

    private Configuration setEnglish() {
        Configuration config = new Configuration();
        config.locale = Locale.ENGLISH;
        ApplicationContextProvider.getContext().getResources().updateConfiguration(config, null);

        return config;
    }
}
