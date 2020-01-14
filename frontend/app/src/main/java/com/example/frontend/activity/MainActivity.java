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
    public static SharedPreferences pref;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static int userId;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get token and id // does not work
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    "mySecuredPrefs",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            editor = sharedPreferences.edit();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace by the real id of the user located in the shared pref
        userId = sharedPreferences.getInt("id", -1) ;
        token = sharedPreferences.getString("token", "");
        Toast.makeText(this, userId + " " + token, Toast.LENGTH_SHORT).show();
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


        // set logged in boolean to false
        editor.putBoolean("signed in",false);
        editor.putString("token", "");
        editor.apply();
        /**
         * Method attached to the LOGOUT button that redirects to the SignInActivity when clicking the button
         * @param Button LOGOUT
         */

        Intent toSignInActivityIntent = new Intent();
        toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
        startActivity(toSignInActivityIntent);

    }
}
