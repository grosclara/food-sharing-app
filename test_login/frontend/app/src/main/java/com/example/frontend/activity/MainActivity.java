package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.frontend.R;
import com.example.frontend.api.NetworkClient;

/**
 * Launcher activity that allows navigating to the Collect and Add activities
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent toLoginActivityIntent = new Intent();
        toLoginActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
        startActivity(toLoginActivityIntent);
    }
}
