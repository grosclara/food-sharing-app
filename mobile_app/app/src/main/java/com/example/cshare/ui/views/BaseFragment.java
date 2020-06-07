package com.example.cshare.ui.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Abstract BaseFragment class, inheriting from the Fragment class, within which the most
 * frequently used methods are redefined.
 *
 * @see Fragment
 *
 * @since 1.1
 * @author Clara Gros
 * @atuhor Babacar Toure
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getFragmentLayout();

    protected abstract void configureDesign(View view);

    protected abstract void updateDesign();

    /**
     * Configures ViewModels with default ViewModelProvider
     *
     * @see androidx.lifecycle.ViewModelProvider
     */
    protected abstract void configureViewModel();

    /**
     * Calls the public methods of our ViewModel to observe their results.
     * <p>
     * For the Get methods, we used the observe() method to be automatically alerted if the
     * database result changes.
     */
    protected abstract void observeDataChanges();

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
        this.configureDesign(view);
        this.configureViewModel();
        this.observeDataChanges();
        return (view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Update Design (Developer will call this method instead of override onActivityCreated())
        this.updateDesign();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
