package com.example.cshare.Controllers.Fragments;

import android.util.Log;

import com.example.cshare.R;

public class SettingsFragment extends BaseFragment {

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected BaseFragment newInstance() { return ( new SettingsFragment() ); }

    @Override
    protected int getFragmentLayout() { return ( R.layout.fragment_settings ); }

    @Override
    protected void configureDesign() {
    }

    @Override
    protected void updateDesign() {}

    // --------------
    // UPDATE UI
    // --------------
}
