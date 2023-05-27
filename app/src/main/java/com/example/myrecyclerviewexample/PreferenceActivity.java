package com.example.myrecyclerviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myrecyclerviewexample.base.BaseActivity;

public class PreferenceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}