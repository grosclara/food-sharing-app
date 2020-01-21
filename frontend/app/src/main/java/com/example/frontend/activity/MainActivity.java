package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.frontend.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
    }

    public void fromMainToCollectActivity(View view) {
        /**
         * Redirect to the CollectActivity when clicking the buttonCollect
         * @param Button buttonCollect
         */
        Intent toCollectActivityIntent = new Intent();
        toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
        startActivity(toCollectActivityIntent);
    }

    public void fromMainToAddActivity(View view) {
        /**
         * Redirect to the AddActivity when clicking the buttonCollect
         * @param Button buttonAdd
         */
        Intent toAddActivityIntent = new Intent();
        toAddActivityIntent.setClass(getApplicationContext(), AddActivity.class);
        startActivity(toAddActivityIntent);
    }

    public void fromMainToCartActivity(View view) {
        /**
         * Method attached to the Shopping Cart button that redirects to the CartActivity when clicking the button
         * @param Button buttonCart
         */
        Intent toCartActivityIntent = new Intent();
        toCartActivityIntent.setClass(getApplicationContext(), CartActivity.class);
        startActivity(toCartActivityIntent);
    }

    public void fromMainToSignInActivity(View view) {

        /**
         * Method attached to the LOGOUT button that redirects to the SignInActivity when clicking the button
         * @param Button LOGOUT
         */

        Intent toSignInActivityIntent = new Intent();
        toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
        startActivity(toSignInActivityIntent);

    }
}
