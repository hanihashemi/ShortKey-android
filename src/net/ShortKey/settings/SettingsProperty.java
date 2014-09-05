package net.ShortKey.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.ShortKey.R;
import net.ShortKey.ApplicationContextProvider;

/**
 * Created by hani on 7/30/14.
 */
public class SettingsProperty {
    public boolean getCheckboxService() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getBoolean(ApplicationContextProvider.getContext().getString(R.string.key_checkbox_run_service), true);
    }

    public void setCheckboxService(boolean value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(ApplicationContextProvider.getContext().getString(R.string.key_checkbox_run_service), value);
        editor.commit();
    }

    public boolean getCheckboxEnableWhenScreenIsOff() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getBoolean(ApplicationContextProvider.getContext().getString(R.string.key_checkbox_enable_action), false);
    }

    public boolean getCheckboxEnableWhenMusicIsPlay() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getBoolean(ApplicationContextProvider.getContext().getString(R.string.key_checkbox_enable_when_music_is_playing), false);
    }

    public int getDefaultVolume()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getInt(ApplicationContextProvider.getContext().getString(R.string.key_default_volume), 14);
    }

    public void setDefaultVolume(int value)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(ApplicationContextProvider.getContext().getString(R.string.key_default_volume), value);
        editor.commit();
    }

    public String getUpAction() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getString(ApplicationContextProvider.getContext().getString(R.string.key_list_on_played_volume_up), ApplicationContextProvider.getContext().getString(R.string.value_of_action_none));
    }

    public String getDownAction() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getString(ApplicationContextProvider.getContext().getString(R.string.key_list_on_played_volume_down), ApplicationContextProvider.getContext().getString(R.string.value_of_action_none));
    }

    public String getDoubleUpAction() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getString(ApplicationContextProvider.getContext().getString(R.string.key_list_on_played_volume_double_up), ApplicationContextProvider.getContext().getString(R.string.value_of_action_none));
    }

    public String getDoubleDownAction() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getString(ApplicationContextProvider.getContext().getString(R.string.key_list_on_played_volume_double_down), ApplicationContextProvider.getContext().getString(R.string.value_of_action_none));
    }

    public String getLanguage()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext());
        return prefs.getString(ApplicationContextProvider.getContext().getString(R.string.key_list_languages), ApplicationContextProvider.getContext().getString(R.string.value_of_language_english));
    }
}
