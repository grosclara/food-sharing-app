package com.example.cshare.Controllers.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.viewModels.ProfileViewModel;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    //ViewModel
    ProfileViewModel profileViewModel;

    View view;

    //Views
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewCampus;
    private TextView textViewRoomNumber;
    private ImageView imageViewProfilePicture;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewCampus = view.findViewById(R.id.textViewCampus);
        textViewRoomNumber = view.findViewById(R.id.textViewRoomNumber);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);


        configureViewModel();
        return view;
    }


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
