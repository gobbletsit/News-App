package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by gobov on 6/2/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SETTING THE ROOT VIEW
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // USING THE PREFERENCES FROM AN XML FILE
            addPreferencesFromResource(R.xml.settings_main_menu);

            // FINDING THE KEY PREFERENCE VALUE AND BINDING IT
            Preference content = findPreference(getString(R.string.q_by_key));
            bindPreferenceSummaryToValue(content);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // PARSING AN OBJECT VALUE TO A STRING
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                // FOR GETTING THE ENTRIES AND SETTING THE LABELS ON THEM
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        // FOR GETTING THE PREFERENCE KEY AND BINDING IT TO A CERTAIN VALUE IN ON CREATE
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }


}
