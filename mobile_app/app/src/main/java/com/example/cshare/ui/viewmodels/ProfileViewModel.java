package com.example.cshare.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.UserResponse;
import com.example.cshare.data.models.User;
import com.example.cshare.data.sources.AuthRequestManager;
import com.example.cshare.data.sources.ProfileRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * ViewModel class responsible for preparing and managing the profile-related data for
 * an Activity or a Fragment.
 * <p>
 * This class provides one the one hand getter methods to retrieve data from the attributes.
 * One the other hand,  it provides methods that will directly call request manager methods to
 * perform some actions on the data
 *
 * @see AndroidViewModel
 * @see ProfileRequestManager
 * @see MutableLiveData
 * @see UserResponse
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class ProfileViewModel extends AndroidViewModel {

    /**
     * Repository that will fetch the auth-related data from the remote API source
     */
    private ProfileRequestManager profileRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<UserResponse> userProfileMutableLiveData;
    private MutableLiveData<UserResponse> otherProfileMutableLiveData;

    /**
     * Constructor of the ViewModel.
     * Takes in an Application parameter that is provided to retrieve the request manager via the
     * getInstance method
     *
     * @param application
     * @throws GeneralSecurityException
     * @throws IOException
     * @see AuthRequestManager#getInstance(Application)
     */
    public ProfileViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);
        // Get request manager instance
        profileRequestManager = profileRequestManager.getInstance(application);

        // Retrieve user profile list from request manager
        userProfileMutableLiveData = profileRequestManager.getUserProfileResponse();
        otherProfileMutableLiveData = profileRequestManager.getOtherUserProfileResponse();
    }

    // Getter method
    public MutableLiveData<UserResponse> getUserProfileMutableLiveData() { return userProfileMutableLiveData; }
    public MutableLiveData<UserResponse> getOtherProfileMutableLiveData() {return otherProfileMutableLiveData; }

    /**
     * Calls the getUserByID method of the request manager
     *
     * @param userID (int)
     * @see ProfileRequestManager#getUserByID(int)
     */
    public void getUserByID(int userID){ profileRequestManager.getUserByID(userID);}
    /**
     * Either calls the editWithPicture or editWithoutPicture method of the request manager
     * depending on the editProfileForm.
     *
     * @param editProfileForm (User)
     * @see ProfileRequestManager#editProfileWithPicture(User)
     * @see ProfileRequestManager#editProfileWithoutPicture(User)
     */
    public void editProfile(User editProfileForm){
        if (editProfileForm.getProfilePictureBody() != null){
            profileRequestManager.editProfileWithPicture(editProfileForm);
        } else { profileRequestManager.editProfileWithoutPicture(editProfileForm); }
    }
}
