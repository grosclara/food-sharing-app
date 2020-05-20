package com.example.cshare.ui.views.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.R;
import com.example.cshare.ui.viewmodels.AuthViewModel;
import com.example.cshare.ui.views.HomeScreenActivity;

/**
 * Launcher activity that redirects the user either to the LoginActivity if he is not logged in,
 * or to the HomeScreenActivity if he is.
 * <p>
 * To find out the log state, the activity implements the authViewModel to retrieve the
 * corresponding data.
 *
 * @see AppCompatActivity
 * @see AuthViewModel
 * @see LoginActivity
 * @see HomeScreenActivity
 * @since 1.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class LauncherActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // VM business logic
        configureViewModel();

        redirect();
    }

    /**
     * Configures ViewModels with default ViewModelProvider
     *
     * @see androidx.lifecycle.ViewModelProvider
     */
    private void configureViewModel() {
        authViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);
    }

    /**
     * Retrieves the boolean log state data from the authViewModel and redirects the user to the
     * appropriate activity.
     * <p>
     * If the user is already logged in, redirects to the HomeScreenActivity.
     * Otherwise, redirects to the LoginActivity.
     *
     * @see AuthViewModel#getIsLoggedInMutableLiveData()
     * @see LoginActivity
     * @see HomeScreenActivity
     */
    private void redirect() {
        if (authViewModel.getIsLoggedInMutableLiveData().getValue()) {
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
}
