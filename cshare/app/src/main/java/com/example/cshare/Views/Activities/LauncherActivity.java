package com.example.cshare.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cshare.R;
import com.example.cshare.ViewModels.AuthViewModel;

public class LauncherActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // Instantiate view model
        authViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AuthViewModel.class);
        authViewModel.getIsLoggedInMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggedIn) {
                if (isLoggedIn) {
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
        });
    }

}
