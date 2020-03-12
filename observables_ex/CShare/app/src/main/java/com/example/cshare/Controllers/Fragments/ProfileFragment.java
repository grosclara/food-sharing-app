package com.example.cshare.Controllers.Fragments;

import com.example.cshare.R;

public class ProfileFragment extends BaseFragment {

    // A fragment that is contained in its parent activity, HomeActivity

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected BaseFragment newInstance() { return (new ProfileFragment()); }

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_profile; }

    @Override
    protected void configureDesign() { }

    @Override
    protected void updateDesign() {}

    // --------------
    // UPDATE UI
    // --------------

}
