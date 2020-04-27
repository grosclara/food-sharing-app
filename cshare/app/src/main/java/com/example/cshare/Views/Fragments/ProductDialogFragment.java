package com.example.cshare.Views.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.cshare.Models.ApiResponses.UserReponse;
import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProductDialogFragment extends DialogFragment {

    private ProductDialogListener listener;

    private Product product;
    private String tag;
    private User supplier;

    // Bind views
    private TextView textViewProductName;
    private TextView textViewProductStatus;
    private TextView textViewProductCategory;
    private TextView textViewExpirationDate;
    private ImageView imageViewProduct;
    private TextView textViewSupplierFirstName;
    private TextView textViewSupplierLastName;
    private TextView textViewSupplierRoomNumber;
    private TextView textViewSupplierCampus;
    private ImageView imageViewSupplierProfilePicture;

    public interface ProductDialogListener {
        void onOrderClicked(Product product, User supplier);
        void onDeleteClicked(Product product);
        void onDeliverClicked(Product product);
        void onCancelOrderClicked(Product product);
    }

    public ProductDialogFragment(Product product, User supplier, String tag) {
        this.product = product;
        this.supplier = supplier;
        this.tag = tag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_dialog_product, null);
        builder.setView(view);

        // Bind product related views
        textViewProductName = view.findViewById(R.id.textViewProductName);
        textViewProductCategory = view.findViewById(R.id.textViewProductCategory);
        textViewProductStatus = view.findViewById(R.id.textViewProductStatus);
        textViewExpirationDate = view.findViewById(R.id.textViewExpirationDate);
        imageViewProduct = view.findViewById(R.id.imageViewProduct);
        // Bind supplier related views
        textViewSupplierFirstName = view.findViewById(R.id.textViewSupplierFirstName);
        textViewSupplierLastName = view.findViewById(R.id.textViewSupplierLastName);
        textViewSupplierCampus = view.findViewById(R.id.textViewSupplierCampus);
        textViewSupplierRoomNumber = view.findViewById(R.id.textViewSupplierRoomNumber);
        imageViewSupplierProfilePicture = view.findViewById(R.id.imageViewSupplierProfilePicture);

        // Fill in product related views
        fillInProductDetails();
        // Retrieve all information from the supplier and fill in the views
        fillInSupplierDetails();

        // Depending on the tag of the dialog, display its title and buttons
        switch (tag) {
            case Constants.ORDER:
                builder.setTitle("Order the product")
                        .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onOrderClicked(product, supplier);
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                break;

            case Constants.SHARED:
                builder.setTitle("Product shared by you")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onDeleteClicked(product);
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                break;

            case Constants.INCART:
                builder.setTitle("What to do with this product?")
                        .setPositiveButton("Delivered", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onDeliverClicked(product);
                            }
                        })
                        .setNegativeButton("Cancel the order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onCancelOrderClicked(product);
                            }
                        });
                break;

            case Constants.ARCHIVED:
                builder.setTitle("Transaction done, enjoy!");
                break;

            default:
                // code block
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void fillInProductDetails() {

        textViewProductName.setText(product.getName());
        textViewProductCategory.setText(product.getCategory());
        textViewProductStatus.setText(product.getStatus());
        switch (product.getStatus()) {
            case "Available":
                textViewProductStatus.setTextColor(Color.parseColor("#5CB85C"));
                break;
            case "Collected":
                textViewProductStatus.setTextColor(Color.parseColor("#f0960c"));
                break;
            case "Delivered":
                textViewProductStatus.setTextColor(Color.parseColor("#f0230c"));
                break;
            default:
        }
        textViewExpirationDate.setText(product.getExpiration_date());
        Picasso.get().load(product.getProduct_picture()).into(imageViewProduct);
    }

    private void fillInSupplierDetails() {
        // HTTP Request to retrieve the user information
        textViewSupplierFirstName.setText(supplier.getFirstName());
        textViewSupplierLastName.setText(supplier.getLastName());
        textViewSupplierCampus.setText(supplier.getCampus());
        textViewSupplierRoomNumber.setText(supplier.getRoomNumber());
        Picasso.get().load(supplier.getProfilePictureURL()).into(imageViewSupplierProfilePicture);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ProductDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement ProductDialogListener");
        }
    }

}
