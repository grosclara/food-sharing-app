package com.example.cshare.Controllers.Fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends BaseFragment {

    // ViewModel
    private ProfileViewModel profileViewModel;

    //Views
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewCampus;
    private TextView textViewRoomNumber;
    private ImageView imageViewProfilePicture;


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

    }

    @Override
    protected void updateDesign() {

    }

    @Override
    protected void configureViewModel() {
        // Retrieve data from view model
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
}
