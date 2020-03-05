package com.example.cshare.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.cshare.Controllers.Fragments.CollectFragment;
import com.example.cshare.Controllers.Fragments.CollectedProductsFragment;
import com.example.cshare.Controllers.Fragments.GivenProductsFragment;
import com.example.cshare.Controllers.Fragments.ProfileFragment;
import com.example.cshare.Controllers.Fragments.SettingsFragment;
import com.example.cshare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    // FOR DESIGN
    @BindView(R.id.activity_home_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_home_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_home_nav_view)
    NavigationView navigationView;
    @BindView(R.id.add_product_fab)
    FloatingActionButton fab;

    // FOR FRAGMENTS
    // Declare fragment handled by Navigation Drawer
    private Fragment fragmentCollect;
    private Fragment fragmentProfile;
    private Fragment fragmentSettings;
    private Fragment fragmentCollectedProducts;
    private Fragment fragmentGivenProducts;

    // FOR DATAS
    // Identify each fragment with a number
    private static final int FRAGMENT_COLLECT = 0;
    private static final int FRAGMENT_PROFILE = 2;
    private static final int FRAGMENT_SETTINGS = 3;
    private static final int FRAGMENT_COLLECTED_PRODUCTS = 4;
    private static final int FRAGMENT_GIVEN_PRODUCTS = 1;

    // --------------
    // BASE METHODS
    // --------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Binding views
        ButterKnife.bind(this);

        // Call all our configuration methods from the onCreate() method of our activity
        // Configure all views
        configureDesign();

        // Show First Fragment
        this.showFirstFragment();
    }

    // Redefine the onBackPressed() method to make the NavigationDrawer close (if open)
    // when the user presses the return key on their phone.
    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Since the interface NavigationView.OnNavigationItemSelectedListener is implemented,
    // we need to declare the method responsible for retrieving clicks on the menu,
    // namely onNavigationItemSelected. This will allow us, depending on the identifier of an item
    // (declared in the file activity_home_menu_drawerl) of the menu, to perform a specific
    // action, such as displaying a page for example.
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id) {
            case R.id.activity_home_drawer_profile:
                this.showFragment(FRAGMENT_PROFILE);
                break;
            case R.id.activity_home_drawer_collect:
                this.showFragment(FRAGMENT_COLLECT);
                break;
            case R.id.activity_home_drawer_settings:
                this.showFragment(FRAGMENT_SETTINGS);
                break;
            case R.id.activity_home_drawer_collected_products:
                this.showFragment(FRAGMENT_COLLECTED_PRODUCTS);
                break;
            case R.id.activity_home_drawer_given_products:
                this.showFragment(FRAGMENT_GIVEN_PRODUCTS);
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    // Release of a default fragment when the activity is created
    //  Attempts to retrieve the fragment displayed in the FrameLayout fragment container.
    //  If no fragment is displayed, then we display the ProductListFragment.
    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_home_drawer_layout);
        if (visibleFragment == null) {
            // Show Collect Fragment
            this.showFragment(FRAGMENT_COLLECT);
            // Mark as selected the menu item corresponding to CollectFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    // Show fragment according an Identifier

   private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_COLLECTED_PRODUCTS:
                this.showCollectedProductsFragment();
                break;
            case FRAGMENT_GIVEN_PRODUCTS:
                this.showGivenProductsFragment();
                break;
            case FRAGMENT_PROFILE:
                this.showProfileFragment();
                break;
            case FRAGMENT_COLLECT:
                this.showCollectFragment();
                break;
            case FRAGMENT_SETTINGS:
                this.showSettingsFragment();
                break;
            default:
                break;
        }
    }

    // Create each fragment page and show it
   private void showCollectedProductsFragment() {
        // Try to find existing instance of fragment in FrameLayout container
        // If no fragments are displayed in our FrameLayout, we create a new one.
        // To do this, we instantiate a new ProductListFragment object.
        if (this.fragmentCollectedProducts == null) this.fragmentCollectedProducts = new CollectedProductsFragment();
        this.startTransactionFragment(this.fragmentCollectedProducts);
    }

    private void showGivenProductsFragment() {
        if (this.fragmentGivenProducts == null) this.fragmentGivenProducts = new GivenProductsFragment();
        this.startTransactionFragment(this.fragmentGivenProducts);
    }

    private void showSettingsFragment() {
        if (this.fragmentSettings == null) this.fragmentSettings = new SettingsFragment();
        this.startTransactionFragment(this.fragmentSettings);
    }

    private void showCollectFragment() {
        if (this.fragmentCollect == null) this.fragmentCollect = new CollectFragment();
        this.startTransactionFragment(this.fragmentCollect);
    }

    private void showProfileFragment() {
        if (this.fragmentProfile == null) this.fragmentProfile = new ProfileFragment();
        this.startTransactionFragment(this.fragmentProfile);
    }

    // Generic method that will replace and show a fragment inside the HomeActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            // Get our FragmentManager & FragmentTransaction (Inside an activity)
            // Use of SupportFragmentManager instead of FragmentManager to support
            // Android versions lower than 3.0
            getSupportFragmentManager().beginTransaction()
                    // Add it to FrameLayout container
                    .replace(R.id.activity_home_frame_layout, fragment).commit();
        }
    }

    // --------------
    // UPDATE UI
    // --------------

    // ---------------------
    // CONFIGURATION
    // ---------------------

    protected void configureDesign() {
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureFab();
    }

    // Configure Toolbar
    private void configureToolBar() {
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    // Retrieve the DrawerLayout, and create from it and the toolbar, the famous "Hamburger" button.
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    // Retrieve the NavigationView so that it can record itself to the activity listener via the
    // interfaceNavigationView.OnNavigationItemSelectedListener), allowing to retrieve menu clicks.
    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Configure floating action button
    private void configureFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });
    }
}
