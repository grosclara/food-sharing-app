package com.example.cshare.ui.views.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.cshare.R;
import com.example.cshare.ui.viewmodels.AuthViewModel;
import com.example.cshare.ui.views.HomeScreenActivity;

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
                    toMainActivityIntent.setClass(getApplicationContext(), HomeScreenActivity.class);
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
