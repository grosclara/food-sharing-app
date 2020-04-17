package com.example.cshare.Views.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.Views.Activities.LauncherActivity;
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
    private Button logOutButton;

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

        // Activate buttons
        logOutButton.setOnClickListener(this);

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

        authViewModel.getIsLoggedInMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedIn) {
                if (! loggedIn) {

                    Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();

                    // Go back to the Launcher Activity
                    Intent toLoginActivityIntent = new Intent();
                    toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                    startActivity(toLoginActivityIntent);
                }
            }
        });

        profileViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {

            @Override
            public void onChanged(User profile) { updateUserDetails(profile); }
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
        }
    }
}
