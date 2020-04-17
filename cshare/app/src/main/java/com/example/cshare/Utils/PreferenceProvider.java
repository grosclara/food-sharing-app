package com.example.cshare.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.cshare.Models.LoginResponse;
import com.example.cshare.Models.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PreferenceProvider {

    private SharedPreferences prefs;
    private Context appContext;
    private String masterKeyAlias;

    private static final String PREFS_NAME = "CShareUserFile";

    private static final String LOG_STATUS_KEY = "logStatus";
    private static final String TOKEN_KEY = "token";
    private static final String ID_KEY = "id";
    private static final String CAMPUS_KEY = "campus";

    public PreferenceProvider(Context context) throws GeneralSecurityException, IOException {
        this.appContext = context.getApplicationContext();

        getMasterKey();

        // Initialize/open an instance of EncryptedSharedPreferences
        this.prefs = EncryptedSharedPreferences.create(
                PREFS_NAME,
                this.masterKeyAlias,
                appContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    // Create or retrieve the Master Key for encryption/decryption
    private void getMasterKey() throws GeneralSecurityException, IOException {
        if (this.masterKeyAlias == null) {
            this.masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }
    }

    public boolean isLoggedIn() {
        Boolean logStatus = prefs.getBoolean(LOG_STATUS_KEY, false);
        return logStatus;
    }

    public void fillPrefs(LoginResponse loginResponse) {
        prefs.edit()
                .putString(TOKEN_KEY, loginResponse.getKey())
                .putString(CAMPUS_KEY, loginResponse.getUser().getCampus())
                .putInt(ID_KEY, loginResponse.getUser().getId())
                .putBoolean(LOG_STATUS_KEY, true)
                .apply();
    }

    public String getToken() {
        return "token "+prefs.getString(TOKEN_KEY, "Invalid token");
    }

    public String getCampus() {
        return prefs.getString(CAMPUS_KEY, "Invalid Campus");
    }

    public int getUserID() {
        return prefs.getInt(ID_KEY, -1);
    }

}


