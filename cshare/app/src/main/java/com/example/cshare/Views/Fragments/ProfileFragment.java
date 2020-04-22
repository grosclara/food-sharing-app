package com.example.cshare.Views.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.UserReponse;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Views.Activities.LoginActivity;
import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.ViewModels.AuthViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    // ViewModel
    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;

    //Views
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewCampus;
    private TextView textViewRoomNumber;
    private ImageView imageViewProfilePicture;

    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmNewPassword;

    private Button logOutButton;
    private Button changePasswordButton;
    private Button deleteAccountButton;

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
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewCampus = view.findViewById(R.id.textViewCampus);
        textViewRoomNumber = view.findViewById(R.id.textViewRoomNumber);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        logOutButton = view.findViewById(R.id.buttonLogOut);
        changePasswordButton = view.findViewById(R.id.buttonChangePassword);
        deleteAccountButton = view.findViewById(R.id.buttonDeleteAccount);


        // Activate buttons
        logOutButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        deleteAccountButton.setOnClickListener(this);

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

        profileViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User profile) {
                updateUserDetails(profile);
            }
        });
    }

    private void updateUserDetails(User profile) {
        textViewFirstName.setText(profile.getFirstName());
        textViewLastName.setText(profile.getLastName());
        textViewCampus.setText(profile.getCampus());
        textViewEmail.setText(profile.getEmail());
        textViewRoomNumber.setText(profile.getRoomNumber());
        Picasso.get().load(profile.getProfilePictureURL()).into(imageViewProfilePicture);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogOut:
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
                break;

            case R.id.buttonChangePassword:
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
                break;
            case R.id.buttonDeleteAccount:
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
    }
}
