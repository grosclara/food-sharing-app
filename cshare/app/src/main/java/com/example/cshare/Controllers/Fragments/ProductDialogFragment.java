package com.example.cshare.Controllers.Fragments;

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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
import com.squareup.picasso.Picasso;

public class ProductDialogFragment extends DialogFragment {

    public Product product;
    public Context context;
    public String state;

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

    public ProductDialogFragment(Context context, Product product, String state) {
        this.context = context;
        this.product = product;
        this.state = state;
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

        // Bind supplier related views
        fillInSupplierDetails(view, product.getSupplier());
        // Bind product related views
        fillInProductDetails(view);

        // Depending on the state of the dialog, display its title and buttons
        switch (state) {
            case "order":
                builder.setTitle("Order the product")
                        .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // Order the product
                                // Create the order object
                                if (product.getStatus().equals("Available")) {
                                    // Change the status attribute of the product object to not available
                                    //updateProductStatus(product, "Collected");
                                    Log.d(Constants.TAG, "Status updated");
                                }
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                break;

            default:
                // code block
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void fillInProductDetails(View view){

        // Bind views
        textViewProductName = view.findViewById(R.id.textViewProductName);
        textViewProductCategory = view.findViewById(R.id.textViewProductCategory);
        textViewProductStatus = view.findViewById(R.id.textViewProductStatus);
        textViewExpirationDate = view.findViewById(R.id.textViewExpirationDate);
        imageViewProduct = view.findViewById(R.id.imageViewProduct);

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

    private void fillInSupplierDetails(View view, int supplierID){
        // Retrieve all information from the supplier and fill in the views

        // Bind views
        textViewSupplierFirstName = view.findViewById(R.id.textViewSupplierFirstName);
        textViewSupplierLastName = view.findViewById(R.id.textViewSupplierLastName);
        textViewSupplierCampus = view.findViewById(R.id.textViewSupplierCampus);
        textViewSupplierRoomNumber = view.findViewById(R.id.textViewSupplierRoomNumber);
        imageViewSupplierProfilePicture = view.findViewById(R.id.imageViewSupplierProfilePicture);

        // HTTP Request to retrieve the user information
        textViewSupplierFirstName.setText("Clara");
        textViewSupplierLastName.setText("Gros");
        textViewSupplierCampus.setText("Gif");
        textViewSupplierRoomNumber.setText("4F306");
        Picasso.get().load(R.drawable.photo_cv).into(imageViewSupplierProfilePicture);

    }



}
