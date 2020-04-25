package com.example.cshare.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.cshare.Models.Response.LoginResponse;

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
        return prefs.getBoolean(LOG_STATUS_KEY, false);
    }

    public void  logOut(){
        prefs.edit().putBoolean(LOG_STATUS_KEY, false).apply();
    }

    public void fillPrefs(LoginResponse loginResponsetest) {
        prefs.edit()
                .putString(TOKEN_KEY, loginResponsetest.getToken())
                .putString(CAMPUS_KEY, loginResponsetest.getUser().getCampus())
                .putInt(ID_KEY, loginResponsetest.getUser().getId())
                .putBoolean(LOG_STATUS_KEY, true)
                .apply();
    }

    public String getToken() {
        return "token "+prefs.getString(TOKEN_KEY, "invalidToken");
    }

    public String getCampus() {
        return prefs.getString(CAMPUS_KEY, "invalidCampus");
    }

    public int getUserID() {
        return prefs.getInt(ID_KEY, -1);
    }

    public void updateCampus(String campus){
        prefs.edit()
                .putString(CAMPUS_KEY, campus)
                .apply();
    }

}


