package net.ShortKey.settings;

/**
 * Created by hani on 8/8/14.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import net.ShortKey.ApplicationContextProvider;
import net.ShortKey.MultiLanguage;
import net.ShortKey.R;
import net.ShortKey.service.ShortKeyServiceController;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        handlePreferences();
        reloadStatus();
    }

    private void reloadStatus() {
        SettingsProperty settingsProperty = new SettingsProperty();

        settingsProperty.setCheckboxService(ApplicationContextProvider.isSCheckServerRunning());

        CheckBoxPreference chkService = (CheckBoxPreference) findPreference(ApplicationContextProvider.getContext().getString(R.string.key_checkbox_run_service));
        chkService.setChecked(settingsProperty.getCheckboxService());
    }

    private void handlePreferences() {
        Preference myPref;

        myPref = findPreference(getString(R.string.key_contact_us));
        myPref.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        reloadStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_checkbox_run_service)))
            onRunServiceClicked();
        else if (key.equals(getString(R.string.key_checkbox_enable_action)))
            new ShortKeyServiceController().restart();
        else if (key.equals(getString(R.string.key_checkbox_enable_when_music_is_playing)))
            new ShortKeyServiceController().restart();
        else
            onItemClicked(key);
    }

    private void onRunServiceClicked() {
        new ShortKeyServiceController().ToggleServiceStatus();
    }

    private void onItemClicked(String key) {
//        ListPreference lp = (ListPreference) findPreference(key);
//        if (lp == null) return;

//        lp.setSummary("dummy"); // required or will not update
//        lp.setSummary(getString(R.string.pref_yourKey) + ": %s");

        if (key.equals(getString(R.string.key_list_languages))) {
            onConfigurationChanged(new MultiLanguage().setLanguage());
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.key_contact_us))) {
            final Intent result = new Intent(android.content.Intent.ACTION_SEND);
            result.setType("plain/text");
            result.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"jhanihashemi@gmail.com"});
            result.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback");
            result.putExtra(android.content.Intent.EXTRA_TEXT, "");

            startActivity(result);
            return true;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }
}