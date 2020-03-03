package com.example.cshare.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cshare.Controllers.Fragments.AddFragment;
import com.example.cshare.Controllers.Fragments.CartFragment;
import com.example.cshare.Controllers.Fragments.HomeFragment;
import com.example.cshare.Controllers.Fragments.ProfileFragment;
import com.example.cshare.Controllers.Fragments.SharedFragment;
import com.example.cshare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // FOR DESIGN
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNav;
   // @BindView(R.id.fragment_container)
    //Fragment fragment;

    // FOR FRAGMENTS
    // Declare fragment handled by Navigation Drawer
    private Fragment fragmentShared;
    private Fragment fragmentProfile;
    private Fragment fragmentCart;
    private Fragment fragmentHome;
    private Fragment fragmentAdd;

    // FOR DATAS
    // Identify each fragment with a number
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_SHARED = 2;
    private static final int FRAGMENT_CART = 3;
    private static final int FRAGMENT_ADD = 4;


    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding views
        ButterKnife.bind(this);

        // Call all our configuration methods from the onCreate() method of our activity
        // Configure all views
        configureDesign();

        this.showFirstFragment(savedInstanceState);

    }

    // Since the interface BottomNavigationView.OnNavigationItemSelectedListener is implemented,
    // we need to declare the method responsible for retrieving clicks on the menu,
    // namely onNavigationItemSelected. This will allow us, depending on the identifier of an item
    // (declared in the file activity_home_menu_drawer) of the menu, to perform a specific
    // action, such as displaying a page for example.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id) {
            case R.id.nav_profile:
                this.showFragment(FRAGMENT_PROFILE);
                break;
            case R.id.nav_home:
                this.showFragment(FRAGMENT_HOME);
                break;
            case R.id.nav_cart:
                this.showFragment(FRAGMENT_CART);
                break;
            case R.id.nav_shared:
                this.showFragment(FRAGMENT_SHARED);
                break;
            case R.id.nav_add:
                this.showFragment(FRAGMENT_ADD);
                break;
        }

        return true;
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    private void showFirstFragment(Bundle savedInstanceState){
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    // Create each fragment page and show it
    private void showFragment(int fragmentId) {
        // Try to find existing instance of fragment in FrameLayout container
        // If no fragments are displayed in our FrameLayout, we create a new one.
        // To do this, we instantiate a new Fragment object.
        switch (fragmentId) {
            case FRAGMENT_HOME:
                if (this.fragmentHome == null) this.fragmentHome = new HomeFragment();
                this.startTransactionFragment(this.fragmentHome);
                break;
            case FRAGMENT_PROFILE:
                if (this.fragmentProfile == null) this.fragmentProfile = new ProfileFragment();
                this.startTransactionFragment(this.fragmentProfile);
                break;
            case FRAGMENT_CART:
                if (this.fragmentCart == null) this.fragmentCart = new CartFragment();
                this.startTransactionFragment(this.fragmentHome);
                break;
            case FRAGMENT_SHARED:
                if (this.fragmentShared == null) this.fragmentShared = new SharedFragment();
                this.startTransactionFragment(this.fragmentHome);
                break;
            case FRAGMENT_ADD:
                if (this.fragmentAdd == null) this.fragmentAdd = new AddFragment();
                this.startTransactionFragment(this.fragmentAdd);
                break;
            default:
                break;
        }
    }

    // Generic method that will replace and show a fragment inside the HomeActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            // Get our FragmentManager & FragmentTransaction (Inside an activity)
            // Use of SupportFragmentManager instead of FragmentManager to support
            // Android versions lower than 3.0
            getSupportFragmentManager().beginTransaction()
                    // Add it to FrameLayout container
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    protected void configureDesign() {
        this.configureBottomNavigationView();
    }

    // Configure Bottom Navigation View
    // Retrieve the BottomNavigationView so that it can record itself to the activity listener via the
    // interface BottomNavigationView.OnNavigationItemSelectedListener), allowing to retrieve menu clicks.
    private void configureBottomNavigationView(){
        bottomNav.setOnNavigationItemSelectedListener(this);
    }
}
