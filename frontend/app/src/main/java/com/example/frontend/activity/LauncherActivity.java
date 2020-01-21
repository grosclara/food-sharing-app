package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.frontend.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LauncherActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "CShareUserFile";

    public static SharedPreferences userCredits;
    public static SharedPreferences.Editor userCreditsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // Check if the user is logged in
        userCredits = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userCreditsEditor = userCredits.edit();
        Boolean logStatus = userCredits.getBoolean("logStatus", false);

        if(logStatus){
            Intent toCollectActivityIntent = new Intent();
            toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
            startActivity(toCollectActivityIntent);
        } else {
            Intent toSignInActivityIntent = new Intent();
            toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
            startActivity(toSignInActivityIntent);
        }
    }
}
