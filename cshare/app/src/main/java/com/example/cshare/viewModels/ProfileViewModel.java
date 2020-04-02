package com.example.cshare.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.ProfileRequestMananger;


public class ProfileViewModel extends ViewModel {
    private ProfileRequestMananger profileRequestMananger;
    private MutableLiveData<User> userProfileMutableLiveData;

    public ProfileViewModel() {
        // Get request manager instance
        profileRequestMananger = profileRequestMananger.getInstance();
        // Retrieve user profile list from request manager
        userProfileMutableLiveData = profileRequestMananger.getUser();
    }

    // Getter method
    public MutableLiveData<User> getUserMutableLiveData() {
        return userProfileMutableLiveData;
    }
}
