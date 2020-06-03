package com.example.cshare.ui.views.productlists;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.cshare.R;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.apiresponses.UserResponse;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.utils.Constants;
import com.squareup.picasso.Picasso;

/**
 * Class inheriting from DialogFragment which allows the creation of product-related alertDialogs,
 * fully integrated in the life cycle of the fragment in which it is called.
 * <p>
 * These dialogs allow the user to interact with the products and according to the parameters
 * passed to the constructor, the created dialog is customized to meet a specific need
 * (such as ordering a product for example).
 *<p>
 * To create such a dialog of a product, it is necessary to retrieve the data of the users
 * who have shared the product through the ProfileViewModel class.
 *<p>
 * Finally, to perform actions when the user clicks on a button, the class has a listener interface.
 *
 * @see DialogFragment
 * @see ProductDialogFragment.ProductDialogListener
 * @see ProfileViewModel
 * @see Product
 * @see User
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class ProductDialogFragment extends DialogFragment {

    private ProductDialogListener listener;

    /**
     * Listener interface to handle clicks on a productDialogFragment
     */
    public interface ProductDialogListener {
        void onOrderClicked(Product product);
        void onDeleteClicked(Product product);
        void onDeliverClicked(Product product);
        void onCancelOrderClicked(Product product);
    }

    private Product product;

    /**
     * String that defines the actions that can be performed on a clicked product
     */
    private String tag;
    private Activity activity;

    protected ProfileViewModel profileViewModel;

    // Declare views
    private TextView textViewProductStatus;
    private TextView textViewProductCategory;
    private TextView textViewExpirationDate;
    private ImageView imageViewProduct;
    private TextView textViewSupplier;
    private TextView textViewSupplierRoomNumber;
    private TextView textViewSupplierCampus;
    private ImageView imageViewSupplierProfilePicture;

    /**
     * Constructor of the class.
     * As soon as the object is created, the constructor calls the getUserByID method of the
     * ProfileViewModel which loads the data of the supplier into the ViewModel.
     *
     * @param product
     * @param tag
     * @see Product
     * @see ProfileViewModel#getUserByID(int)
     */
    public ProductDialogFragment(Activity activity, Product product, String tag) {
        this.product = product;
        this.tag = tag;
        this.activity = activity;

        configureViewModel();
        loadSupplierData();

    }

    private void configureViewModel(){
        // Retrieve view models
        profileViewModel = new ViewModelProvider((ViewModelStoreOwner) activity,
                new ViewModelProvider.AndroidViewModelFactory(activity.getApplication())
        ).get(ProfileViewModel.class);
    }

    private void loadSupplierData(){
        profileViewModel.getUserByID(product.getSupplier());
        getSupplierInfo();
    }

    /**
     * Creates the alertDialog according to the class attributes.
     * <p>
     * First bind the view to the builder and retrieve all information of the customer and the
     * supplier through the profileViewModel.
     * Then, fill in the views thanks to the information by calling the getSupplierInfo and
     * fillInProductDetails methods.
     * The string tag determines the action to perform and then the button that have to be added on
     * the dialog. Each button will execute one of the interface method.
     *
     * @param savedInstanceState (Bundle)
     * @return (Dialog) AlertDialog to be displayed
     * @see Dialog
     * @see ProfileViewModel#getUserByID(int)
     * @see ProfileViewModel#getOtherProfileMutableLiveData()
     * @see #fillInProductDetails(Product)
     * @see #getSupplierInfo()
     * @see ProductDialogListener
     * @see User
     * @see Product
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_dialog_product, null);
        builder.setView(view);

        // Bind product related views
        textViewProductCategory = view.findViewById(R.id.textViewProductCategory);
        textViewProductStatus = view.findViewById(R.id.textViewProductStatus);
        textViewExpirationDate = view.findViewById(R.id.textViewExpirationDate);
        imageViewProduct = view.findViewById(R.id.imageViewProduct);
        // Bind supplier related views
        textViewSupplier = view.findViewById(R.id.textViewSupplier);
        textViewSupplierCampus = view.findViewById(R.id.textViewSupplierCampus);
        textViewSupplierRoomNumber = view.findViewById(R.id.textViewSupplierRoomNumber);
        imageViewSupplierProfilePicture = view.findViewById(R.id.imageViewSupplierProfilePicture);

        // Fill in product related views
        fillInProductDetails(product);

        // Depending on the tag of the dialog, display its title and buttons
        switch (tag) {
            // User wants to order the product
            case Constants.ORDER:
                builder.setTitle(product.getName())
                        .setPositiveButton(R.string.order, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onOrderClicked(product);
                            }
                        })
                        .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                break;
            // User wants to delete the product he shared
            case Constants.SHARED:
                builder.setTitle(product.getName())
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onDeleteClicked(product);
                            }
                        })
                        .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                break;
            // Either mark as delivered or cancel order
            case Constants.INCART:
                builder.setTitle(product.getName())
                        .setPositiveButton(R.string.delievered, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onDeliverClicked(product);
                            }
                        })
                        .setNegativeButton(R.string.cancel_order, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onCancelOrderClicked(product);
                            }
                        });
                break;
            // See the archived product
            case Constants.ARCHIVED:
                builder.setTitle(R.string.transaction_done);
                break;
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ProductDialogListener) context;
    }

    /**
     * Observe the supplier response data from the profileViewModel.
     * <p>
     * After a request to retrieve the supplier data, the response status changes to success or
     * failure.
     * In case of a successful response, calls the fillInSupplierDetails method to fill in the
     * supplier related views. In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see UserResponse
     * @see User
     * @see #fillInSupplierDetails(User)
     */
    private void getSupplierInfo(){
        profileViewModel.getOtherProfileMutableLiveData().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    // Retrieve the supplier from the response and call the fillInSupplier method
                    fillInSupplierDetails(response.getUser());
                    profileViewModel.getOtherProfileMutableLiveData().setValue(UserResponse.complete());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    profileViewModel.getOtherProfileMutableLiveData().setValue(UserResponse.complete());
                }
            }
        });
    }

    /**
     * Fills in the product-related views with the information retrieved from the product object
     *
     * @param product (Product) product item on which the user has clicked
     * @see Product
     */
    private void fillInProductDetails(Product product) {
        // Set text
        textViewProductCategory.setText(product.getCategory());
        textViewExpirationDate.setText(product.getExpiration_date());
        textViewProductStatus.setText(product.getStatus());
        // Set status policy
        switch (product.getStatus()) {
            case Constants.AVAILABLE:
                textViewProductStatus.setTextColor(getContext().getColor(R.color.colorAvailable));
                break;
            case Constants.COLLECTED:
                textViewProductStatus.setTextColor(getContext().getColor(R.color.colorCollected));
                break;
            case Constants.DELIVERED:
                textViewProductStatus.setTextColor(getContext().getColor(R.color.colorDelivered));
                break;
            default:
        }
        // Set product picture
        Picasso.get().load(product.getProduct_picture()).into(imageViewProduct);
    }

    /**
     * Fills in the supplier-related views with the information retrieved from the supplier object
     *
     * @param supplier (User) supplier of the product item on which the user has clicked
     * @see User
     */
    private void fillInSupplierDetails(User supplier) {
        // HTTP Request to retrieve the user information
        textViewSupplier.setText(supplier.getFirstName()+" "+supplier.getLastName());
        textViewSupplierCampus.setText(supplier.getCampus());
        textViewSupplierRoomNumber.setText(supplier.getRoomNumber());
        Picasso.get().load(supplier.getProfilePictureURL()).into(imageViewSupplierProfilePicture);
    }
}
