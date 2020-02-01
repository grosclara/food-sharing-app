package com.example.frontend.activity.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.frontend.R;
import com.example.frontend.activity.CollectActivity;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.example.frontend.model.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderProductDialogFragment extends DialogFragment {

    public Product product;
    public Context context;

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

    public OrderProductDialogFragment(Context context, Product product) {
        this.context = context;
        this.product = product;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_product_details, null);
        builder.setView(view);

        getUserById(view, product.getSupplier());

        textViewProductName = view.findViewById(R.id.textViewProductName);
        textViewProductName.setText(product.getName());
        textViewProductCategory = view.findViewById(R.id.textViewProductCategory);
        textViewProductCategory.setText(product.getCategory());
        textViewProductStatus = view.findViewById(R.id.textViewProductStatus);
        textViewProductStatus.setText(String.valueOf(product.getIs_available()));
        textViewExpirationDate = view.findViewById(R.id.textViewExpirationDate);
        textViewExpirationDate.setText(product.getExpiration_date());
        imageViewProduct = view.findViewById(R.id.imageViewProduct);
        Picasso.get().load(product.getProduct_picture()).into(imageViewProduct);

        builder.setTitle("Order the product")
                .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Order the product
                        // Create the order object
                        Order order = new Order(CollectActivity.userId, product.getId());
                        // Post order
                        addOrder(order);
                        // Change the is_available attribute of the product object to not available
                        updateProduct(product);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Intent toCollectActivityIntent = new Intent();
        toCollectActivityIntent.setClass(context, CollectActivity.class);
        startActivity(toCollectActivityIntent);
        super.onDismiss(dialog);
    }

    public void getUserById(View view, int supplierId) {
        /**
         * Send a HTTP request to retrieve all information from the user
         * @param supplierId
         */

        // Retrieve a reference on the textViews defined in the xml layout file
        textViewSupplierFirstName = view.findViewById(R.id.textViewSupplierFirstName);
        textViewSupplierLastName = view.findViewById(R.id.textViewSupplierLastName);
        textViewSupplierCampus = view.findViewById(R.id.textViewSupplierCampus);
        textViewSupplierRoomNumber = view.findViewById(R.id.textViewSupplierRoomNumber);
        imageViewSupplierProfilePicture = view.findViewById(R.id.imageViewSupplierProfilePicture);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.getUserByID(CollectActivity.token, CollectActivity.userId);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    textViewSupplierFirstName.setText(response.body().getFirst_name());
                    textViewSupplierLastName.setText(response.body().getLast_name());
                    textViewSupplierCampus.setText(response.body().getCampus());
                    textViewSupplierRoomNumber.setText(response.body().getRoom_number());
                    Picasso.get().load(response.body().getProfile_picture()).into(imageViewSupplierProfilePicture);

                } else {
                    Toast.makeText(context, "An error occurred to retrieve the supplier info!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    public void addOrder(Order order) {
        /**
         * Take into param an order and add it to the remote database asynchronously
         * @param order
         */

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<Order> call = djangoRestApi.addOrder(CollectActivity.token, order);
        // Asynchronous request
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(context, "You order the product!", Toast.LENGTH_SHORT).show();

                } else {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    public void updateProduct(Product product) {
        // PB WITH THE PICTURE FIELD
        /**
         * Take into param a product and update it in the remote database asynchronously
         * @param product
         */

        // Retrieve the id of the product
        int productId = product.getId();
        // Set attributes to null so that they are not changed in the db by the HTTP PATCH request
        product.setProduct_picture(null);
        product.setCreated_at(null);
        product.setName(null);
        product.setQuantity(null);
        product.setCategory(null);
        product.setExpiration_date(null);
        // Set its is_available attribute to false as it has just been order by someone
        product.setIs_available(false);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<Product> call = djangoRestApi.updateProduct(CollectActivity.token, productId, product);
        // Asynchronous request
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(context, "Submit!", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(context, "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

}
