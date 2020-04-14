package com.example.cshare.Controllers.Fragments;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfileFragment extends BaseFragment implements View.OnClickListener{

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

    //User credits
    String token;
    int userId;

    private Button logoutButton;

    @Override
    protected BaseFragment newInstance() { return new ProfileFragment(); }

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_profile; }

    @Override
    protected void configureDesign(View view) {

        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewCampus = view.findViewById(R.id.textViewCampus);
        textViewRoomNumber = view.findViewById(R.id.textViewRoomNumber);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);

        logoutButton =view.findViewById(R.id.buttonLogOut);

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
                textViewFirstName.setText(profile.getFirstName());
                textViewLastName.setText(profile.getLastName());
                textViewCampus.setText(profile.getCampus());
                textViewEmail.setText(profile.getEmail());
                textViewRoomNumber.setText(profile.getRoomNumber());
                Picasso.get().load(profile.getProfilePictureURL()).into(imageViewProfilePicture);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonLogOut:
                authViewModel.logout(token);
                authViewModel.getLoggedOutMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean loggedOut) {
                        if (loggedOut = true){
                            LauncherActivity.userCreditsEditor.putBoolean("logStatus",false);
                            LauncherActivity.userCreditsEditor.apply();

                            Intent toLoginActivityIntent = new Intent();
                            toLoginActivityIntent.setClass(getContext(), LoginActivity.class);
                            startActivity(toLoginActivityIntent);

                        }
                    }
                });



        }
    }
}
