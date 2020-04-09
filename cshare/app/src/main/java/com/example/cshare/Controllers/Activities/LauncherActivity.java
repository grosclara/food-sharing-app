package com.example.cshare.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cshare.R;

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
            Intent toMainActivityIntent = new Intent();
            toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(toMainActivityIntent);
            finish();
        } else {
            Intent toSignInActivityIntent = new Intent();
            toSignInActivityIntent.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(toSignInActivityIntent);
            finish();
        }

    }
}
