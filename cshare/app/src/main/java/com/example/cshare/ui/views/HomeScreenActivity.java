package com.example.cshare.ui.views;

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

import com.example.cshare.data.models.Order;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.utils.Constants;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.R;
import com.example.cshare.ui.views.productlists.CartFragment;
import com.example.cshare.ui.views.productlists.HomeFragment;
import com.example.cshare.ui.views.productlists.ProductDialogFragment;
import com.example.cshare.ui.views.productlists.SharedFragment;
import com.example.cshare.utils.MediaFiles;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import static com.example.cshare.utils.MediaFiles.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.example.cshare.utils.MediaFiles.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;


public class HomeScreenActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ProductDialogFragment.ProductDialogListener {

    BottomNavigationView bottomNav;
    BaseFragment selectedFragment;

    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding views
        bottomNav = findViewById(R.id.bottom_navigation);

        // Call all our configuration methods from the onCreate() method of our activity
        configureViewModel();

        // Configure all views
        configureDesign();

        // Grant permissions
        grantWriteExternalStoragePermission();
        grantReadExternalStoragePermission();

        // Show first fragment when creating this activity
        this.showFirstFragment(savedInstanceState);
    }

    /**
     * Each time the activity is created, the application checks that permission to read external
     * files is granted. If this is not the case, the application asks the device for permission.
     *
     * @see #checkSelfPermission(String)
     * @see #requestPermissions(String[], int)
     */
    private void grantReadExternalStoragePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission has already been granted
            Log.d(Constants.TAG, "Permission read ext storage granted");
        }
    }

    /**
     * Each time the activity is created, the application checks that permission to write external
     * files is granted. If this is not the case, the application asks the device for permission.
     * The callback method gets the result of the request.
     *
     * @see #checkSelfPermission(String)
     * @see #requestPermissions(String[], int)
     */
    private void grantWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission has already been granted
            Log.d(Constants.TAG, "Permission write ext storage granted");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Retrieve the id of the clicked item in the menu
        int id = menuItem.getItemId();
        selectedFragment = null;

        // Create and show corresponding fragment after user clicked on a menu item
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

        // Generic method that will replace and show a fragment inside the HomeScreenActivity
        // Frame Layout

        getSupportFragmentManager().beginTransaction()
                // Add it to FrameLayout container
                .replace(R.id.fragment_container,
                        selectedFragment).commit();
        return true;
    }

    /**
     * Show first fragment when activity is created
     * @param savedInstanceState
     */
    private void showFirstFragment(Bundle savedInstanceState) {
        // if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            selectedFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
        }
    }

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
    private void configureBottomNavigationView() {
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
    public void onDeliverClicked(Product product) {
        // Set status to delivered and send request to update in database
        Map<String, String> status = new HashMap<>();
        status.put("status", Constants.DELIVERED);
        productViewModel.deliver(product.getId());
    }

    @Override
    public void onCancelOrderClicked(Product product) {
        // Delete order and set product status to available
        Map<String, String> status = new HashMap<>();
        status.put("status", Constants.AVAILABLE);
        productViewModel.cancelOrder(product.getId());
    }

}
