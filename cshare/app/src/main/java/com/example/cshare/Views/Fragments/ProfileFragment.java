package com.example.cshare.Views.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.EditProfileForm;
import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.UserReponse;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Utils.Camera;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.Views.Activities.LoginActivity;
import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.ViewModels.AuthViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    // ViewModel
    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;
    private ProductViewModel productViewModel;

    //Views
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Spinner spinnerCampus;
    private EditText editTextRoomNumber;
    private ImageView imageViewProfilePicture;
    private TextView textViewEmail;

    private String[] campusArray;

    private String campus;
    private String firstName;
    private String lastName;
    private String roomNumber;

    private EditProfileForm editProfileForm;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

    private Boolean validated = true;

    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmNewPassword;

    private Button buttonSave;
    private Button logOutButton;
    private Button changePasswordButton;
    private Button deleteAccountButton;
    private Button buttonGallery;

    @Override
    protected BaseFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void configureDesign(View view) {

        // Bind views
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        spinnerCampus = view.findViewById(R.id.spinnerCampus);
        editTextRoomNumber = view.findViewById(R.id.editTextRoomNumber);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);

        textViewEmail = view.findViewById(R.id.textViewEmail);

        // Buttons
        logOutButton = view.findViewById(R.id.buttonLogOut);
        changePasswordButton = view.findViewById(R.id.buttonChangePassword);
        deleteAccountButton = view.findViewById(R.id.buttonDeleteAccount);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonGallery = view.findViewById(R.id.buttonGallery);

        // Activate buttons
        logOutButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        deleteAccountButton.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonGallery.setOnClickListener(this);
        imageViewProfilePicture.setOnClickListener(this);

        configureCampusSpinner();

    }

    @Override
    protected void updateDesign() {
    }

    @Override
    protected void configureViewModel() {
        // Retrieve auth data from view model (log out, change password)
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        // Retrieve user details from profile view model
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        authViewModel.getChangePasswordMutableLiveData().observe(this, new Observer<ApiEmptyResponse>() {
            @Override
            public void onChanged(ApiEmptyResponse apiEmptyResponse) {
                if (apiEmptyResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (apiEmptyResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                } else if (apiEmptyResponse.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), apiEmptyResponse.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    authViewModel.getChangePasswordMutableLiveData().setValue(ApiEmptyResponse.complete());
                }
            }
        });

        authViewModel.getLogoutResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ApiEmptyResponse>() {
            @Override
            public void onChanged(ApiEmptyResponse apiEmptyResponse) {
                if (apiEmptyResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (apiEmptyResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the Login activity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);

                } else if (apiEmptyResponse.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), apiEmptyResponse.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    authViewModel.getLogoutResponseMutableLiveData().setValue(ApiEmptyResponse.complete());
                }
            }
        });

        authViewModel.getDeleteResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserReponse>() {
            @Override
            public void onChanged(UserReponse response) {
                if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the Login activity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);

                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    authViewModel.getDeleteResponseMutableLiveData().setValue(UserReponse.complete());
                }
            }
        });

        profileViewModel.getUserProfileMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserReponse>() {
            @Override
            public void onChanged(UserReponse response) {
                if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "User info retrieved successfully", Toast.LENGTH_SHORT).show();
                    updateUserDetails(response.getUser());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    //TODO to check
                    profileViewModel.getUserProfileMutableLiveData().setValue(UserReponse.complete());
                }
            }
        });

        profileViewModel.getEditedProfileMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ApiEmptyResponse>() {
            @Override
            public void onChanged(ApiEmptyResponse response) {
                Log.d(Constants.TAG, "onChanged edited" + response.getStatus());
                if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Profile info edited successfully", Toast.LENGTH_SHORT).show();
                    productViewModel.update();
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserDetails(User profile) {
        textViewEmail.setText(profile.getEmail());
        editTextLastName.setText(profile.getLastName());
        editTextFirstName.setText(profile.getFirstName());
        spinnerCampus.setSelection(((ArrayAdapter) spinnerCampus.getAdapter()).getPosition(profile.getCampus()));
        textViewEmail.setText(profile.getEmail());
        editTextRoomNumber.setText(profile.getRoomNumber());
        Picasso.get().load(profile.getProfilePictureURL()).into(imageViewProfilePicture);
    }

    @Override
    public void onClick(View v) {

        if (v == logOutButton) {
            // Alert Dialog to confirm the will to sign out
            // Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            // Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Are you sure you want to log out ?")
                    .setTitle("Log out")
                    // Add the buttons
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button -> logout the user
                            authViewModel.logOut();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (v == changePasswordButton) {
            // Alert Dialog to change password
            // Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builderPassword = new AlertDialog.Builder(getContext());

            // Inflate the layout
            View changePasswordLayout = LayoutInflater.from(getContext()).inflate(R.layout.change_password, null);

            // Load the edit texts
            editTextOldPassword = changePasswordLayout.findViewById(R.id.editTextOldPassword);
            editTextNewPassword = changePasswordLayout.findViewById(R.id.editTextNewPassword);
            editTextConfirmNewPassword = changePasswordLayout.findViewById(R.id.editTextConfirmNewPassword);

            builderPassword.setView(changePasswordLayout);

            // Chain together various setter methods to set the dialog characteristics
            builderPassword.setMessage("Enter your old password and a new one")
                    .setTitle("Change Password")
                    // Add the buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button -> change password
                            PasswordForm passwordForm = new PasswordForm(editTextOldPassword.getText().toString().trim(),
                                    editTextNewPassword.getText().toString().trim(),
                                    editTextConfirmNewPassword.getText().toString().trim());

                            // Validation
                            // TODO : set Validators
                            if (passwordForm.isValid()) {
                                authViewModel.changePassword(passwordForm);
                            } else {
                                // Set errors
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Get the AlertDialog from create()
            AlertDialog dialogPassword = builderPassword.create();
            dialogPassword.show();
        }

        if (v == deleteAccountButton) {

            // Alert Dialog to confirm the will to delete account
            // Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder deleteAccountBuilder = new AlertDialog.Builder(getContext());
            // Chain together various setter methods to set the dialog characteristics
            deleteAccountBuilder.setMessage("Are you sure you want to delete your profile? This action is irreversible.")
                    .setTitle("Delete account")
                    // Add the buttons
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button -> delete the user's account
                            authViewModel.deleteAccount();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Get the AlertDialog from create()
            AlertDialog deleteAccountDialog = deleteAccountBuilder.create();
            deleteAccountDialog.show();
        }

        if (v == buttonSave) {
            //TODO Validate form

            // Validate the field
            //validator.validate();

            if (validated) {

                // Retrieve user details from the edit text
                lastName = editTextLastName.getText().toString().trim();
                firstName = editTextFirstName.getText().toString().trim();
                roomNumber = editTextRoomNumber.getText().toString().trim();

                editProfileForm = new EditProfileForm(lastName, firstName, roomNumber, campus);

                if (fileToUploadUri != null) {

                    fileToUpload = new File(fileToUploadPath);
                    // Create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileToUploadUri)), fileToUpload);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part profilePictureBody = MultipartBody.Part.createFormData("profile_picture", fileToUploadPath, requestFile);

                    editProfileForm.setProfile_picture(profilePictureBody);
                }

                profileViewModel.editProfile(editProfileForm);

            }
        }

        if (v == buttonGallery || v == imageViewProfilePicture) {
            Camera.choosePictureFromGallery(null, this);
        }
    }

    /**
     * Receiving activity result method will be called after closing the gallery
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == Camera.CAMERA_CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            // data.getData returns the content URI for the selected Image
            pictureFileUri = data.getData();

            // modify the raw picture taken in a new file and retrieve its Uri
            try {
                fileToUpload = Camera.processPicture(getContext(), pictureFileUri);

                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = Camera.getOutputMediaFileUri(getContext(), fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewProfilePicture);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getContext(),
                    "Sorry! Failed to choose any image", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private void configureCampusSpinner() {
        campusArray = getResources().getStringArray(R.array.campus_array);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, campusArray);
        // Apply the adapter to the spinner
        spinnerCampus.setAdapter(adapterSpinner);
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                campus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
