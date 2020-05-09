package com.example.cshare.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.UserReponse;
import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.ProfileRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class ProfileViewModel extends AndroidViewModel {

    private ProfileRequestManager profileRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<UserReponse> userProfileMutableLiveData;
    private MutableLiveData<UserReponse> otherProfileMutableLiveData;
    private MutableLiveData<UserReponse> editedProfileMutableLiveData;

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
    public MutableLiveData<UserReponse> getEditedProfileMutableLiveData() {return editedProfileMutableLiveData; }

    public void update(){
        profileRequestManager.update();
    }

    public void getUserByID(int userID){
        profileRequestManager.getUserByID(userID);
    }

    public void editProfile(User editProfileForm){
        if (editProfileForm.getProfilePictureBody() != null){
            profileRequestManager.editProfileWithPicture(editProfileForm);
        } else { profileRequestManager.editProfileWithoutPicture(editProfileForm); }
    }

}
