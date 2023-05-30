package com.example.myrecyclerviewexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.myrecyclerviewexample.base.Parameters;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            //IP preferences with ListPreference
            final EditTextPreference IP = findPreference("IP_key");
            //Port preferences with ListPreference
            final EditTextPreference port = findPreference("port_key");
            //Prefix preferences with ListPreference
            final EditTextPreference prefix = findPreference("prefix_key");

            IP.setSummary(Parameters.IP);
            IP.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    //Cambiamos la IP en el 'EditTextPreference'
                    IP.setSummary(editText.getText());
                    //Cambiamos la IP en nuestro servicio
                    Parameters.IP = editText.getText().toString();
                    Parameters.PREFIX = Parameters.IP + ":" + Parameters.PORT;

                    prefix.setSummary(Parameters.PREFIX);
                }
            });



            port.setSummary(Parameters.PORT);
            port.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    //Cambiamos el puesto en el 'EditTextPreference'
                    port.setSummary(editText.getText());
                    //Cambiamos el puerto en nuestro servicio
                    Parameters.PORT = editText.getText().toString();
                    Parameters.PREFIX = Parameters.IP + ":" + Parameters.PORT;

                    prefix.setSummary(Parameters.PREFIX);

                }
            });


            prefix.setSummary(Parameters.PREFIX);
            prefix.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    //Cambiamos el prefijo en el 'EditTextPreference'
                    if(!(editText.getText().toString().equals(""))){
                        prefix.setSummary(editText.getText().toString());
                        //Cambiamos el prefijo en nuestro servicio
                        Parameters.PREFIX = editText.getText().toString();
                    }

                }
            });

        }
    }


}