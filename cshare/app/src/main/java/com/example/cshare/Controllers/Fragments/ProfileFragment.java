package com.example.cshare.Controllers.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.Controllers.Activities.LauncherActivity;
import com.example.cshare.Controllers.Activities.LoginActivity;
import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
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

    private Button logoutButton;

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

        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewCampus = view.findViewById(R.id.textViewCampus);
        textViewRoomNumber = view.findViewById(R.id.textViewRoomNumber);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);

        logoutButton = view.findViewById(R.id.buttonLogOut);

        logoutButton.setOnClickListener(this);

    }

    @Override
    protected void updateDesign() {
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data from view model
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User profile) {
                textViewFirstName.setText(profile.getFirst_name());
                textViewLastName.setText(profile.getLast_name());
                textViewCampus.setText(profile.getCampus());
                textViewEmail.setText(profile.getEmail());
                textViewRoomNumber.setText(profile.getRoom_number());
                Picasso.get().load(profile.getProfile_picture()).into(imageViewProfilePicture);

            }
        });
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
                                authViewModel.logout(Constants.TOKEN);
                                authViewModel.getLoggedOutMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean loggedOut) {
                                        if (loggedOut) {
                                            LauncherActivity.userCreditsEditor.putBoolean("logStatus", false);
                                            LauncherActivity.userCreditsEditor.apply();

                                            Intent toLoginActivityIntent = new Intent();
                                            toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                                            startActivity(toLoginActivityIntent);

                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                //finish();
                            }
                        });
                // Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
        }
    }
}
