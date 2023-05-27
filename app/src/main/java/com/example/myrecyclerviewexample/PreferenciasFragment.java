package com.example.myrecyclerviewexample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Arrays;
import java.util.List;

public class PreferenciasFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferencias,rootKey);

        //Units preferences with ListPreference
        final ListPreference unidades = findPreference("unidades_key");
        final List<String> unidades_entries = Arrays.asList(getResources().getStringArray(R.array.unidades_entries));
        final List<String> unidades_values = Arrays.asList(getResources().getStringArray(R.array.unidades_values));

        int pos  = unidades_values.indexOf(MyPreferenceManager.getInstance(getContext()).getUnitsListPreference());

        unidades.setSummary("Unidades en " + unidades_entries.get(pos));
        unidades.setOnPreferenceChangeListener((preference, newValue) -> {

            int pos1 = unidades_values.indexOf(newValue);
            unidades.setSummary("Unidades en " + unidades_entries.get(pos1));

            return true;
        });


        // Theme preferences with ListPreference
        ListPreference themePreference = getPreferenceManager().findPreference("themes_key");
        if (themePreference.getValue() == null) {
            themePreference.setValue(ThemeSetup.Mode.DEFAULT.name());
        }
        themePreference.setOnPreferenceChangeListener((p,v)->{
            ThemeSetup.applyTheme(ThemeSetup.Mode.valueOf((String)v));
            return true;
        });

    }
}
