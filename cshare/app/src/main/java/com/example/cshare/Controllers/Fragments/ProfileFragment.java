package com.example.cshare.Controllers.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
import com.example.cshare.databinding.MainBinder;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    MainBinder binding;

    //ViewModel
    ProfileViewModel profileViewModel;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // instantiate binder
        binding = DataBindingUtil.setContentView(getActivity(),R.layout.fragment_profile);

        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        configureViewModel();
        return view;
    }


    protected void configureViewModel() {
        // Retrieve data from view model
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setProfileViewModel(profileViewModel);
        profileViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.textViewFirstName.setText(user.getFirst_name());
                binding.textViewLastName.setText(user.getLast_name());
                binding.textViewEmail.setText(user.getEmail());
                binding.textViewCampus.setText(user.getCampus());
                binding.textViewRoomNumber.setText(user.getRoom_number());
                Picasso.get().load(user.getProfile_picture()).into(binding.imageViewProfilePicture);
            }
        });
    }
}
