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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.cshare.R;
import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.apiresponses.UserResponse;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.ui.viewmodels.CartViewModel;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.ui.viewmodels.SharedViewModel;
import com.example.cshare.ui.views.auth.ProfileFragment;
import com.example.cshare.ui.views.productlists.CartFragment;
import com.example.cshare.ui.views.productlists.HomeFragment;
import com.example.cshare.ui.views.productlists.ProductDialogFragment;
import com.example.cshare.ui.views.productlists.SharedFragment;
import com.example.cshare.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.cshare.utils.MediaFiles.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.example.cshare.utils.MediaFiles.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

/**
 * Main activity of the application that acts as a controller between the product-related and
 * profile BaseFragments.
 * <p>
 * On the one hand it implements a bottomNavigationView and the necessary methods to be able to
 * navigate between all fragments.
 * <p>
 * On the other hand, it listens to some listeners to perform actions on the data
 * whenever a user interacts with a product-related data either via a productDialogFragment or via
 * the ProfileFragment. Moreover, it implements some ViewModels to observe data changes and to
 * update the product lists in each fragment if necessary
 * <p>
 * Eventually it also allow the user to grant the read and write external storage permissions.
 *
 * @see AppCompatActivity
 * @see BaseFragment
 * @see BottomNavigationView
 * @see BottomNavigationView.OnNavigationItemSelectedListener
 * @see ProductDialogFragment.ProductDialogListener
 * @see ProductViewModel
 * @see HomeViewModel
 * @see ProfileViewModel
 * @see CartViewModel
 * @see SharedViewModel
 *
 * @since 1.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class HomeScreenActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ProductDialogFragment.ProductDialogListener {

    BottomNavigationView bottomNav;
    //This is our viewPager
    private ViewPager viewPager;
    MenuItem prevMenuItem;

    // Fragments
    HomeFragment homeFragment;
    SharedFragment sharedFragment;
    CartFragment cartFragment;
    ProfileFragment profileFragment;

    /**
     * Menu index of the home fragment which is the first fragment to be displayed
     */
    private static final int HOME_FRAGMENT_INDEX = 0;

    private static ProductViewModel productViewModel;
    private static HomeViewModel homeViewModel;
    private static SharedViewModel sharedViewModel;
    public static ProfileViewModel profileViewModel;
    private static CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding views
        bottomNav = findViewById(R.id.bottom_navigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);


        // Configure all views
        configureDesign();

        // Grant permissions
        grantWriteExternalStoragePermission();
        grantReadExternalStoragePermission();

        // VM business logic
        configureViewModel();
        observeDataChanges();
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
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                viewPager.setCurrentItem(0);
                getSupportActionBar().setTitle(R.string.home);
                break;
            case R.id.nav_cart:
                viewPager.setCurrentItem(1);
                getSupportActionBar().setTitle(R.string.cart);
                break;
            case R.id.nav_shared:
                viewPager.setCurrentItem(2);
                getSupportActionBar().setTitle(R.string.shared);
                break;
            case R.id.nav_profile:
                viewPager.setCurrentItem(3);
                getSupportActionBar().setTitle(R.string.profile);
                break;
        }
        return false;
    }

    protected void configureDesign() {
        this.configureBottomNavigationView();
        this.configureViewPager();
    }

    private void configureViewPager(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) { prevMenuItem.setChecked(false); }
                else {bottomNav.getMenu().getItem(0).setChecked(false);}
                bottomNav.getMenu().getItem(position).setChecked(true);
                switch (position){
                    case 0:
                        getSupportActionBar().setTitle(R.string.home);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.cart);
                        break;
                    case 2:
                        getSupportActionBar().setTitle(R.string.shared);
                        break;
                    case 3:
                        getSupportActionBar().setTitle(R.string.profile);
                        break;
                    default:
                        getSupportActionBar().setTitle(R.string.app_name);
                }
                prevMenuItem = bottomNav.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        sharedFragment = new SharedFragment();
        profileFragment = new ProfileFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(cartFragment);
        adapter.addFragment(sharedFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
        getSupportActionBar().setTitle(R.string.home);
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

    /**
     * Configures ViewModels with default ViewModelProvider
     *
     * @see androidx.lifecycle.ViewModelProvider
     */
    protected void configureViewModel() {
        productViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(ProductViewModel.class);
        homeViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(HomeViewModel.class);
        sharedViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(SharedViewModel.class);
        cartViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(CartViewModel.class);
        profileViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(ProfileViewModel.class);
    }

    /**
     * Calls the public methods of our ViewModel to observe their results.
     * <p>
     * For the Get methods, we used the observe() method to be automatically alerted if the
     * database result changes.
     */
    private void observeDataChanges(){
        this.getDeleteResponse();
        this.getOrderResponse();
        this.getCancelOrderResponse();
        this.getDeliverResponse();
        this.getUserInfo();
    }

    /**
     * When called this method calls for the refresh method of the view models passed in parameters
     * to update (refresh) the corresponding product lists.
     * <p>
     * If one wants to update only one or two view models, one can pass null as parameter for the
     * model not being updated
     *
     * @param homeViewModel
     * @param sharedViewModel
     * @param cartViewModel
     * @see HomeViewModel#refresh()
     * @see SharedViewModel#refresh()
     * @see CartViewModel#refresh()
     */
    private static void refreshProductLists(HomeViewModel homeViewModel,
                                            SharedViewModel sharedViewModel,
                                            CartViewModel cartViewModel){
        if (homeViewModel != null) { homeViewModel.refresh(); }
        if (sharedViewModel != null) { sharedViewModel.refresh(); }
        if (cartViewModel != null) { cartViewModel.refresh(); }
    }

    /**
     * Observe the user profile response data from the profileViewModel.
     * <p>
     * After a request to retrieve the user details, the response status changes to success or
     * failure. In case of failure, toasts an error message.
     *
     * @see ProfileViewModel#getUserProfileMutableLiveData()
     * @see UserResponse
     */
    private void getUserInfo(){
        profileViewModel.getUserProfileMutableLiveData().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse response) {
                 if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(),
                                response.getError().getDetail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.unexpected_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    /**
     * Observe the delete response data from the productViewModel.
     * <p>
     * After a request to delete the product, the response status changes to success or failure.
     * In case of a successful response, calls the refreshProductList to update data that has
     * changed following the deletion of the product. In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see ProductViewModel#getDeleteProductResponse()
     * @see ProductResponse
     * @see #refreshProductLists(HomeViewModel, SharedViewModel, CartViewModel)
     */
    private void getDeleteResponse(){
        productViewModel.getDeleteProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.deletion_successful, Toast.LENGTH_SHORT).show();
                    refreshProductLists(homeViewModel, sharedViewModel, null);

                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());
                }
            }
        });
    }

    /**
     * Observe the order response data from the productViewModel.
     * <p>
     * After a request to order a product, the response status changes to success or failure.
     * In case of a successful response, calls the refreshProductList to update data that has
     * changed following the order of the product. In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see ProductViewModel#getOrderProductResponse() ()
     * @see ProductResponse
     * @see #refreshProductLists(HomeViewModel, SharedViewModel, CartViewModel)
     */
    private void getOrderResponse(){
        productViewModel.getOrderProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.order_successful, Toast.LENGTH_SHORT).show();
                    refreshProductLists(homeViewModel, null, cartViewModel);

                    productViewModel.getOrderProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getOrderProductResponse().setValue(ProductResponse.complete());
                }
            }
        });
    }

    /**
     * Observe the cancel order response data from the productViewModel.
     * <p>
     * After a request to cancel an order, the response status changes to success or failure.
     * In case of a successful response, calls the refreshProductList to update data that has
     * changed following the cancellation of the order. In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see ProductViewModel#getCancelOrderResponse() ()
     * @see ProductResponse
     * @see #refreshProductLists(HomeViewModel, SharedViewModel, CartViewModel)
     */
    private void getCancelOrderResponse(){
        productViewModel.getCancelOrderResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.cancel_successful, Toast.LENGTH_SHORT).show();
                    refreshProductLists(homeViewModel, null, cartViewModel);
                    productViewModel.getCancelOrderResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getCancelOrderResponse().setValue(ProductResponse.complete());
                }
            }
        });
    }

    /**
     * Observe the deliver response data from the productViewModel.
     * <p>
     * After a request to mark a product as delivered, the response status changes to success or
     * failure.
     * In case of a successful response, calls the refreshProductList to update data that has
     * changed following the delivery of the product. In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see ProductViewModel#getDeliverProductResponse() ()
     * @see ProductResponse
     * @see #refreshProductLists(HomeViewModel, SharedViewModel, CartViewModel)
     */
    private void getDeliverResponse(){
        productViewModel.getDeliverProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.delivery_successful, Toast.LENGTH_SHORT).show();
                    refreshProductLists(null, null, cartViewModel);
                    productViewModel.getDeliverProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getDeliverProductResponse().setValue(ProductResponse.complete());
                }
            }
        });
    }

    /**
     * Method called when a user clicks "Order" in a productDialogFragment
     * When called, this method checks whether the product is still available, creates a new Order
     * object, and call the order method of the product view model
     *
     * @param product (Product) product to be ordered
     * @see Product
     * @see User
     * @see ProductDialogFragment.ProductDialogListener
     * @see ProductViewModel#order(int)
     */
    @Override
    public void onOrderClicked(Product product) {
        if (product.getStatus().equals(Constants.AVAILABLE)) {
            productViewModel.order(product.getId());
        } else {
            Toast.makeText(this,
                    R.string.product_already_ordered,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method called when a user clicks "Delete" in a productDialogFragment
     * When called, this method checks whether the product is still available, and call the
     * delete method of the product view model
     *
     * @param product (Product) product to be deleted
     * @see Product
     * @see ProductDialogFragment.ProductDialogListener
     * @see ProductViewModel#deleteProduct(int)
     */
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

    /**
     * Method called when a user clicks "Delivered" in a productDialogFragment
     * When called, this method calls the deliver method of the product view model
     *
     * @param product (Product) product to be marked as delivered
     * @see Product
     * @see ProductDialogFragment.ProductDialogListener
     * @see ProductViewModel#deliver(int)
     */
    @Override
    public void onDeliverClicked(Product product) {
        productViewModel.deliver(product.getId());
    }

    /**
     * Method called when a user clicks "Cancel order" in a productDialogFragment
     * When called, this method checks whether the product is still available, and call the
     * delete method of the product view model
     *
     * @param product (Product) product to be deleted
     * @see Product
     * @see ProductDialogFragment.ProductDialogListener
     * @see ProductViewModel#cancelOrder(int)
     */
    @Override
    public void onCancelOrderClicked(Product product) {
        if (product.getStatus().equals(Constants.COLLECTED)) {
            productViewModel.cancelOrder(product.getId());
        } else {
            Toast.makeText(this,
                    R.string.product_already_delivered,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method called when a user modifies its campus on the ProfileFragment
     * When called, this method calls the refreshProductLists method
     *
     * @see ProfileFragment
     * @see HomeScreenActivity#refreshProductLists(HomeViewModel,SharedViewModel, CartViewModel)
     */
    public static void onCampusChanged() {
        refreshProductLists(homeViewModel, sharedViewModel, cartViewModel);
    }

    /**
     * Method called when a user add a new product in the AddActivity
     * When called, this method calls the refreshProductLists method
     *
     * @see AddActivity
     * @see HomeScreenActivity#refreshProductLists(HomeViewModel,SharedViewModel, CartViewModel)
     */
    public static void onProductAdded() {
        refreshProductLists(homeViewModel, sharedViewModel, null);
    }
}
