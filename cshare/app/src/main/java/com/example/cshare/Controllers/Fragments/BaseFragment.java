package com.example.cshare.Controllers.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cshare.Controllers.Activities.LauncherActivity;
import com.example.cshare.R;

public abstract class BaseFragment extends Fragment {

    // Abstract BaseFragment class, inheriting from the Fragment class. Inside,
    // we have redefined the most repeated methods

    // Force the developer to implement them in future children's classes.
    protected abstract com.example.cshare.Controllers.Fragments.BaseFragment newInstance();

    protected abstract int getFragmentLayout();

    protected abstract void configureDesign(View view);

    protected abstract void updateDesign();

    protected abstract void configureViewModel();

    // User credits
    private String token;
    private int userId;

    // -----------------
    // METHODS OVERRIDE
    // -----------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Method onCreateView() allows to declare our layout
        // Get layout identifier from abstract method
        View view = inflater.inflate(getFragmentLayout(), container, false);
        // Configure Design (Developer will call this method instead of override onCreateView())
        // allow our child fragments not to have to redefine the onCreateView( ) method,
        // but instead to call configureDesign()

        // Retrieve user credits from SP
        token = "Token "+ LauncherActivity.userCredits.getString("token", null).trim();
        userId = LauncherActivity.userCredits.getInt("id", -1);

        this.configureDesign(view);
        this.configureViewModel();
        return (view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Handling Bundle Restoration
        // Icepick.restoreInstanceState(this, savedInstanceState);
        // Update Design (Developer will call this method instead of override onActivityCreated())
        this.updateDesign();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Handling Bundle Save
        // Icepick.saveInstanceState(this, outState);
    }


}
