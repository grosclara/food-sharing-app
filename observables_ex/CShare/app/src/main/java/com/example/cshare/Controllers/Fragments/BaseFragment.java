package com.example.cshare.Controllers.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import icepick.Icepick;

public abstract class BaseFragment extends Fragment {
    // abstract BaseFragment class, inheriting from the Fragment class. Inside,
    // we have redefined the most repeated methods

    // force the developer to implement them in future children's classes.
    protected abstract BaseFragment newInstance();
    protected abstract int getFragmentLayout();
    protected abstract void configureDesign();
    protected abstract void updateDesign();

    // -----------------
    // METHODS OVERRIDE
    // -----------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Method onCreateView() allows to declare our layout
        // Get layout identifier from abstract method
        View view = inflater.inflate(getFragmentLayout(), container, false);
        // Binding Views
        ButterKnife.bind(this, view);
        // Configure Design (Developer will call this method instead of override onCreateView())
        // allow our child fragments not to have to redefine the onCreateView( ) method,
        // but instead to call configureDesign()
        this.configureDesign();
        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Handling Bundle Restoration
        Icepick.restoreInstanceState(this, savedInstanceState);
        // Update Design (Developer will call this method instead of override onActivityCreated())
        this.updateDesign();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Handling Bundle Save
        Icepick.saveInstanceState(this, outState);
    }
}
