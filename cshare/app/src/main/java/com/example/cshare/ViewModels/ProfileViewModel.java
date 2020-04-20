package com.example.cshare.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.ProfileRequestManager;
import com.example.cshare.Utils.PreferenceProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class ProfileViewModel extends AndroidViewModel {

    private ProfileRequestManager profileRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<User> userProfileMutableLiveData;

    public ProfileViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);
        // Get request manager instance
        profileRequestManager = profileRequestManager.getInstance(application);

        // Retrieve user profile list from request manager
        userProfileMutableLiveData = profileRequestManager.getUser();
    }

    // Getter method
    public MutableLiveData<User> getUserMutableLiveData() { return userProfileMutableLiveData; }

    public void update(){
        Log.d("tag","profile update");
        profileRequestManager.update();
    }

    public int getUserID(){
        return profileRequestManager.getUserID();
    }

}
