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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.cshare.Views.Activities.MainActivity;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProductDialogFragment extends DialogFragment {

    public Product product;
    public Context context;
    public String tag;
    public ProductViewModel productViewModel;

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

    public ProductDialogFragment(Context context, Product product, String tag, ProductViewModel productViewModel) {
        this.context = context;
        this.product = product;
        this.tag = tag;
        this.productViewModel = productViewModel;
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

        // Depending on the tag of the dialog, display its title and buttons
        switch (tag) {
            case Constants.ORDER:
                builder.setTitle("Order the product")
                        .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Order the product
                                if (product.getStatus().equals("Available")) {
                                    // Create the order object
                                    Order request = new Order(MainActivity.userID, product.getId());
                                    // Change the status attribute of the product object to not available
                                    Map<String, String> status = new HashMap<>();
                                    status.put("status", Constants.COLLECTED);
                                    productViewModel.order(request, status);

                                    //updateProductStatus(product, status);
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

            case Constants.SHARED:
                builder.setTitle("Product shared by you")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Check the status
                                if (product.getStatus().equals(Constants.AVAILABLE)) {
                                    // if still available, delete the product from the database
                                    Log.d(Constants.TAG, "Product deleted");
                                    productViewModel.deleteProduct(product);
                                } else {
                                    Toast.makeText(context, "Someone has already ordered the product", Toast.LENGTH_SHORT).show();
                                }
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
                        /*.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) { }
                        })*/
                        .setPositiveButton("Delivered", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Set status to delivered and send request to update in database
                                Map<String, String> status = new HashMap<>();
                                status.put("status", Constants.DELIVERED);
                                productViewModel.deliver(product.getId(), status);
                            }
                        })
                        .setNegativeButton("Cancel the order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Delete order and set product status to available
                                Map<String, String> status = new HashMap<>();
                                status.put("status", Constants.AVAILABLE);
                                productViewModel.cancelOrder(product.getId(), status);
                                //updateProductStatus(product, "Available");
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

    private void fillInProductDetails(View view) {

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

    private void
    fillInSupplierDetails(View view, int supplierID) {
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
