package com.example.cshare.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.example.cshare.Views.Fragments.BaseFragment;
import com.example.cshare.Views.Fragments.CartFragment;
import com.example.cshare.Views.Fragments.HomeFragment;
import com.example.cshare.Views.Fragments.ProductDialogFragment;
import com.example.cshare.Views.Fragments.ProfileFragment;
import com.example.cshare.Views.Fragments.SharedFragment;
import com.example.cshare.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ProductDialogFragment.ProductDialogListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    // FOR DESIGN
    BottomNavigationView bottomNav;
    BaseFragment selectedFragment;

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding views
        // ButterKnife.bind(this);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Call all our configuration methods from the onCreate() method of our activity
        configureViewModel();
        // Configure all views
        configureDesign();

        grantPermission();

        this.showFirstFragment(savedInstanceState);

    }

    private void grantPermission(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d("tag", "permission write ext storage granted");
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Log.d("tag", "permission read ext storage granted");
        }

    }

    // Since the interface BottomNavigationView.OnNavigationItemSelectedListener is implemented,
    // we need to declare the method responsible for retrieving clicks on the menu,
    // namely onNavigationItemSelected. This will allow us, depending on the identifier of an item
    // (declared in the file activity_home_menu_drawer) of the menu, to perform a specific
    // action, such as displaying a page for example.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        selectedFragment = null;

        // Show fragment after user clicked on a menu item
        // Create each fragment page and show it
        switch (id) {
            case R.id.nav_profile:
                selectedFragment = new ProfileFragment();
                break;
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.nav_cart:
                selectedFragment = new CartFragment();
                break;
            case R.id.nav_shared:
                selectedFragment = new SharedFragment();
                break;
        }
        // Generic method that will replace and show a fragment inside the HomeActivity Frame Layout

        // Get our FragmentManager & FragmentTransaction (Inside an activity)
        // Use of SupportFragmentManager instead of FragmentManager to support
        // Android versions lower than 3.0
        getSupportFragmentManager().beginTransaction()
                // Add it to FrameLayout container
                .replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    private void showFirstFragment(Bundle savedInstanceState){
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            selectedFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
        }
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProductViewModel.class);
    }

    protected void configureDesign() {
        this.configureBottomNavigationView();
    }

    // Configure Bottom Navigation View
    // Retrieve the BottomNavigationView so that it can record itself to the activity listener via the
    // interface BottomNavigationView.OnNavigationItemSelectedListener), allowing to retrieve menu clicks.
    private void configureBottomNavigationView(){
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onOrderClicked(Product product, User customer) {
        Log.d(Constants.TAG, "order listener");
        // Order the product
        if (product.getStatus().equals("Available")) {
            // Create the order object
            Order request = new Order(customer.getId(), product.getId());
            // Change the status attribute of the product object to not available
            Map<String, String> status = new HashMap<>();
            status.put("status", Constants.COLLECTED);
            productViewModel.order(request);
        }
    }

    @Override
    public void onDeleteClicked(Product product) {
        Log.d(Constants.TAG, "delete listener");
        // Check the status
        if (product.getStatus().equals(Constants.AVAILABLE)) {
            // if still available, delete the product from the database
            productViewModel.deleteProduct(product.getId());
        } else {
            Toast.makeText(this, "Someone has already ordered the product", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeliverClicked(Product product){
        // Set status to delivered and send request to update in database
        Map<String, String> status = new HashMap<>();
        status.put("status", Constants.DELIVERED);
        productViewModel.deliver(product.getId());
    }

    @Override
    public void onCancelOrderClicked(Product product){
        // Delete order and set product status to available
        Map<String, String> status = new HashMap<>();
        status.put("status", Constants.AVAILABLE);
        productViewModel.cancelOrder(product.getId());
    }

}
