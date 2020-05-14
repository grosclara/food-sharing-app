package com.example.cshare.ui.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.UserReponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.utils.MediaFiles;
import com.example.cshare.utils.Constants;
import com.example.cshare.ui.viewmodels.CartViewModel;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.SharedViewModel;
import com.example.cshare.ui.views.auth.LoginActivity;
import com.example.cshare.data.models.User;
import com.example.cshare.R;
import com.example.cshare.ui.viewmodels.AuthViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment implements View.OnClickListener, Validator.ValidationListener {

    // ViewModel
    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;
    private SharedViewModel sharedViewModel;
    private CartViewModel cartViewModel;
    private HomeViewModel homeViewModel;

    // Form validation
    protected Validator validator;
    private boolean validated;

    //Views
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private Spinner spinnerCampus;
    @NotEmpty
    private EditText editTextRoomNumber;
    private ImageView imageViewProfilePicture;
    private TextView textViewEmail;

    private String[] campusArray;

    private String campus;
    private String firstName;
    private String lastName;
    private String roomNumber;

    private User editProfileForm;
    private User changePasswordForm;

    // Path to the location of the picture taken by the phone
    private Uri pictureFileUri;
    private File fileToUpload;
    private Uri fileToUploadUri;
    private String fileToUploadPath;

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
        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
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

        configureValidator();

    }

    @Override
    protected void updateDesign() {}

    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator((this));
        validator.setValidationListener(this);
    }

    @Override
    protected void configureViewModel() {
        // Retrieve auth data from view model (log out, change password)
        authViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(AuthViewModel.class);
        // Retrieve user details from profile view model
        profileViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);

        // Update VM if the campus is changed
        cartViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CartViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SharedViewModel.class);


        authViewModel.getChangePasswordMutableLiveData().observe(this, new Observer<EmptyAuthResponse>() {
            @Override
            public void onChanged(EmptyAuthResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {
                    authViewModel.getChangePasswordMutableLiveData().setValue(EmptyAuthResponse.complete());
                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.ERROR)) {

                    authViewModel.getChangePasswordMutableLiveData().setValue(EmptyAuthResponse.complete());

                    if (response.getError().getDetail() != null ){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    }
                    else if (response.getError().getOld_password() != null) {
                        Toast.makeText(getContext(), response.getError().getOld_password(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        authViewModel.getLogoutResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<EmptyAuthResponse>() {
            @Override
            public void onChanged(EmptyAuthResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {

                    authViewModel.updateUserCredentials();

                    authViewModel.getLogoutResponseMutableLiveData().setValue(EmptyAuthResponse.complete());

                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the Login activity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);

                } else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null) {

                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                    authViewModel.getLogoutResponseMutableLiveData().setValue(EmptyAuthResponse.complete());
                }
            }
        });

        authViewModel.getDeleteResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<EmptyAuthResponse>() {
            @Override
            public void onChanged(EmptyAuthResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {

                    authViewModel.updateUserCredentials();

                    authViewModel.getDeleteResponseMutableLiveData().setValue(EmptyAuthResponse.complete());

                    Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the Login activity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);

                } else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null) {

                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                    authViewModel.getDeleteResponseMutableLiveData().setValue(EmptyAuthResponse.complete());
                }
            }
        });

        profileViewModel.getUserProfileMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserReponse>() {
            @Override
            public void onChanged(UserReponse response) {
                Log.d(Constants.TAG, "USER PROFILE ON CHANGED "+response.getStatus());

                if (response.getStatus().equals(Status.SUCCESS)) {

                    // profileViewModel.getUserProfileMutableLiveData().setValue(UserReponse.complete());
                    Toast.makeText(getContext(), "User info retrieved successfully", Toast.LENGTH_SHORT).show();
                    updateUserDetails(response.getUser());

                }

                else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null ){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateUserDetails(User profile) {
        textViewEmail.setText(profile.getEmail());
        textViewLastName.setText(profile.getLastName());
        textViewFirstName.setText(profile.getFirstName());
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
                            changePasswordForm = new User(editTextOldPassword.getText().toString().trim(),
                                    editTextNewPassword.getText().toString().trim(),
                                    editTextConfirmNewPassword.getText().toString().trim());

                            // Validation
                            if (changePasswordForm.getOldPassword().isEmpty()){
                                Toast.makeText(getContext(),"Enter your current password", Toast.LENGTH_SHORT).show();
                            }
                            else if (changePasswordForm.getPassword1().length() < 6) {
                                Toast.makeText(getContext(),"Passwords should be at least 6 characters long", Toast.LENGTH_SHORT).show();
                            } else if (!changePasswordForm.getPassword2().equals(changePasswordForm.getPassword2())) {
                                Toast.makeText(getContext(),"Passwords should match", Toast.LENGTH_SHORT).show();
                            } else {
                                authViewModel.changePassword(changePasswordForm);
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

            // Validate the field
            validator.validate();

            if (validated) {

                if (campus != profileViewModel.getUserProfileMutableLiveData().getValue().getUser().getCampus()){
                    sharedViewModel.refresh();
                    homeViewModel.refresh();
                    cartViewModel.refresh();
                }

                // Retrieve user details from the edit text
                roomNumber = editTextRoomNumber.getText().toString().trim();

                editProfileForm = new User(lastName, firstName, roomNumber, campus);

                if (fileToUploadUri != null) {

                    fileToUpload = new File(fileToUploadPath);
                    // Create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileToUploadUri)), fileToUpload);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part profilePictureBody = MultipartBody.Part.createFormData("profile_picture", fileToUploadPath, requestFile);

                    editProfileForm.setProfilePictureBody(profilePictureBody);
                }

                profileViewModel.editProfile(editProfileForm);

            }
        }

        if (v == buttonGallery || v == imageViewProfilePicture) {

            showPictureDialog(this);
        }
    }

    private void showPictureDialog(Fragment fragment) {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(fragment.getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                MediaFiles.choosePictureFromGallery(null, fragment);
                                break;
                            case 1:
                                try {
                                    pictureFileUri = MediaFiles.captureImage(null, fragment);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    /**
     * Receiving activity result method will be called after closing the gallery
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result code is RESULT_OK only if the user selects an Image
        if (requestCode == MediaFiles.CAMERA_CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

            // data.getData returns the content URI for the selected Image
            pictureFileUri = data.getData();

            // modify the raw picture taken in a new file and retrieve its Uri
            try {
                fileToUpload = MediaFiles.processPicture(getActivity(), pictureFileUri);

                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(getActivity(), fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewProfilePicture);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == MediaFiles.CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // successfully captured the image

            try {
                Log.d(Constants.TAG, pictureFileUri.toString());
                fileToUpload = MediaFiles.processPicture(getActivity(), pictureFileUri); // modify the raw picture taken
                fileToUploadPath = fileToUpload.getAbsolutePath();
                fileToUploadUri = MediaFiles.getOutputMediaFileUri(getActivity(), fileToUpload);

                Picasso.get().load(fileToUploadUri).into(imageViewProfilePicture);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getActivity(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getActivity(),
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

    @Override
    public void onValidationSucceeded() {
        //Called when all your views pass all validations.
        validated = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        //Called when there are validation error(s).
        validated = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Log.d(Constants.TAG, message);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
