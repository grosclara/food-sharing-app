package com.example.cshare.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.EditProfileForm;
import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.UserReponse;
import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.ProfileRequestManager;
import com.example.cshare.Utils.PreferenceProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class ProfileViewModel extends AndroidViewModel {

    private ProfileRequestManager profileRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<UserReponse> userProfileMutableLiveData;
    private MutableLiveData<UserReponse> otherProfileMutableLiveData;
    private MutableLiveData<ApiEmptyResponse> editedProfileMutableLiveData;

    public ProfileViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);
        // Get request manager instance
        profileRequestManager = profileRequestManager.getInstance(application);

        // Retrieve user profile list from request manager
        userProfileMutableLiveData = profileRequestManager.getUserProfileResponse();
        otherProfileMutableLiveData = profileRequestManager.getOtherUserProfileResponse();
        editedProfileMutableLiveData = profileRequestManager.getEditedProfileResponse();

    }

    // Getter method
    public MutableLiveData<UserReponse> getUserProfileMutableLiveData() { return userProfileMutableLiveData; }
    public MutableLiveData<UserReponse> getOtherProfileMutableLiveData() {return otherProfileMutableLiveData; }
    public MutableLiveData<ApiEmptyResponse> getEditedProfileMutableLiveData() {return editedProfileMutableLiveData; }

    public void update(){
        profileRequestManager.update();
    }

    public void getUserByID(int userID){
        profileRequestManager.getUserByID(userID);
    }

    public void editProfile(EditProfileForm editProfileForm){
        if (editProfileForm.getProfile_picture() != null){
            profileRequestManager.editProfileWithPicture(editProfileForm);
        } else { profileRequestManager.editProfileWithoutPicture(editProfileForm); }
    }

}
