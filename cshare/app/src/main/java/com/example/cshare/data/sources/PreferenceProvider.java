package com.example.cshare.data.sources;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.cshare.data.apiresponses.LoginResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Class that manages the creation, writing and reading of the shared preferences of the
 * application in a very secure way relying on the Security library of androidx.
 * <p>
 * The constructor of the class allows to initialize or open an encryptedSharedPreference instance
 * and the methods defined in the body of the class allow to retrieve or write data in the file.
 *
 * @see SharedPreferences
 * @see EncryptedSharedPreferences
 * @see MasterKeys
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */

public class PreferenceProvider {

    private SharedPreferences prefs;
    private Context appContext;
    private String masterKeyAlias;

    private static final String PREFS_NAME = "CShareUserFile";
    private static final String LOG_STATUS_KEY = "logStatus";
    private static final String TOKEN_KEY = "token";
    private static final String ID_KEY = "id";

    /**
     * Class constructor that handles the creation of the Master Key for encryption/decryption and
     * that initializes or opens an instance of EncryptedSharedPreferences
     *
     * @param context
     * @throws GeneralSecurityException
     * @throws IOException
     * @see #getMasterKey()
     * @see EncryptedSharedPreferences#create(String, String, Context, EncryptedSharedPreferences.PrefKeyEncryptionScheme, EncryptedSharedPreferences.PrefValueEncryptionScheme)
     */

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

    /**
     * Method that creates the Master Key for encryption/decryption if it doesn't exist yet
     *
     * @see MasterKeys#getOrCreate(KeyGenParameterSpec)
     */
    private void getMasterKey() throws GeneralSecurityException, IOException {
        if (this.masterKeyAlias == null) {
            this.masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }
    }

    /**
     * Retrieves the value corresponding to the logStatus (by default, returns false)
     * <p>
     * If true the user is logged in, otherwise, returns false.
     * @return
     */
    public boolean isLoggedIn() { return prefs.getBoolean(LOG_STATUS_KEY, false); }

    /**
     * Edit the value corresponding to the logStatus to false to specify the user has logged out
     */
    public void  logOut(){ prefs.edit().putBoolean(LOG_STATUS_KEY, false).apply(); }

    /**
     * Following a successful login request, edit the pref file using the information in the
     * LoginResponse object passed in as parameter.
     *
     * @param loginResponse LoginResponse
     */
    public void fillPrefs(LoginResponse loginResponse) {
        prefs.edit()
                .putString(TOKEN_KEY, loginResponse.getToken())
                .putInt(ID_KEY, loginResponse.getUser().getId())
                .putBoolean(LOG_STATUS_KEY, true)
                .apply();
    }

    /**
     * Retrieves the value corresponding to the token (by default, returns invalidToken)
     * @return
     */
    public String getToken() { return "token "+prefs.getString(TOKEN_KEY, "invalidToken"); }

    /**
     * Retrieves the value corresponding to the user ID (by default, returns -1)
     * @return
     */
    public int getUserID() { return prefs.getInt(ID_KEY, -1); }

}


