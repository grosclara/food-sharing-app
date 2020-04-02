package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.ProfileRequestManager;


public class ProfileViewModel extends ViewModel {
    private ProfileRequestManager profileRequestManager;
    private MutableLiveData<User> userProfileMutableLiveData;

    public ProfileViewModel() {
        // Get request manager instance
        profileRequestManager = profileRequestManager.getInstance();
        // Retrieve user profile list from request manager
        userProfileMutableLiveData = profileRequestManager.getUser();
    }

    // Getter method
    public MutableLiveData<User> getUserMutableLiveData() {
        return userProfileMutableLiveData;
    }
}
