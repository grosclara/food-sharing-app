package com.example.cshare.ui.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.R;
import com.example.cshare.data.models.Order;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.ui.views.productlists.CartFragment;
import com.example.cshare.ui.views.productlists.HomeFragment;
import com.example.cshare.ui.views.productlists.ProductDialogFragment;
import com.example.cshare.ui.views.productlists.SharedFragment;
import com.example.cshare.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import static com.example.cshare.utils.MediaFiles.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.example.cshare.utils.MediaFiles.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

/**
 * Main activity of the application that acts as a controller between the product and user profile
 * fragments.
 * <p>
 * On the one hand it implements a bottomNavigationView and the necessary methods to be able to
 * navigate between all fragments.
 * On
 *
 * @see BottomNavigationView
 * @see BottomNavigationView.OnNavigationItemSelectedListener
 * @see ProductDialogFragment.ProductDialogListener
 * @see ProductViewModel
 * @since 1.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class HomeScreenActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ProductDialogFragment.ProductDialogListener {

    BottomNavigationView bottomNav;
    BaseFragment selectedFragment;

    private static final int HOME_FRAGMENT_INDEX = 0;

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
        this.showFirstFragment();
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
     * Show first fragment (which is Home Fragment) when activity is created
     */
    private void showFirstFragment() {

        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (visibleFragment == null){
            // Show Home Fragment
            visibleFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    visibleFragment).commit();
            // Mark as selected the menu item corresponding to HomeFragment
            this.bottomNav.getMenu().getItem(HOME_FRAGMENT_INDEX).setChecked(true);
        }
    }

    protected void configureDesign() {
        this.configureBottomNavigationView();
    }

    /**
     * Configuring productViewModel with default ViewModelProvider
     *
     * @see ProductViewModel
     * @see ViewModelProvider
     */
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(ProductViewModel.class);
    }

    /**
     * Configure Bottom Navigation View
     * <p>
     * Retrieve the BottomNavigationView so that it can record itself to the activity listener via
     * the interface BottomNavigationView.OnNavigationItemSelectedListener, allowing to retrieve
     * menu clicks.
     *
     * @see com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
     */
    private void configureBottomNavigationView() {
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onOrderClicked(Product product, User customer) {
        // Order the product
        if (product.getStatus().equals(Constants.AVAILABLE)) {
            // Create the order object
            Order request = new Order(customer.getId(), product.getId());
            // Change the status attribute of the product object to not available
            Map<String, String> status = new HashMap<>();
            status.put(Constants.STATUS, Constants.COLLECTED);
            productViewModel.order(request);
        }
    }

    @Override
    public void onDeleteClicked(Product product) {
        // Check the status
        if (product.getStatus().equals(Constants.AVAILABLE)) {
            // if still available, delete the product from the database
            productViewModel.deleteProduct(product.getId());
        } else {
            Toast.makeText(this,
                    R.string.product_already_ordered,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeliverClicked(Product product) {
        // Set status to delivered and send request to update in database
        Map<String, String> status = new HashMap<>();
        status.put(Constants.STATUS, Constants.DELIVERED);
        productViewModel.deliver(product.getId());
    }

    @Override
    public void onCancelOrderClicked(Product product) {
        // Delete order and set product status to available
        Map<String, String> status = new HashMap<>();
        status.put(Constants.STATUS, Constants.AVAILABLE);
        productViewModel.cancelOrder(product.getId());
    }

}
