package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.example.frontend.model.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Possibility to retrieve the information of both the product and the supplier of the product we
 * want to order.
 * For now the button Order is of zero utility (possibility to change statuses)
 * After having ordered the product, the user is redirected to the MainActivity
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */
public class OrderActivity extends AppCompatActivity {

    private TextView textViewProductName;
    private TextView textViewProductStatus;
    private TextView textViewSupplierName;
    private TextView textViewSupplierFirstName;
    private ImageView imageViewProduct;

    private Product product;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Get product info in this new activity
        Intent toOrderActivityIntent = getIntent();
        product = (Product) toOrderActivityIntent.getSerializableExtra("product");

        // Retrieve and display the supplier information in the upper Linear Layout
        int userID = product.getSupplier();
        getUserById(userID);

        // Retrieve the views from the xml file
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewProductStatus = findViewById(R.id.textViewProductStatus);
        imageViewProduct = findViewById(R.id.imageViewProduct);

        // Display the product info in the xml file
        textViewProductName.setText(product.getName());
        if(product.getIs_available()){
            textViewProductStatus.setText("Available");
        } else{
            textViewProductStatus.setText("Already ordered by someone else");
        }
        Picasso.get().load(product.getProduct_picture()).into(imageViewProduct);
    }

    public void getUserById(int userId){

        textViewSupplierFirstName = findViewById(R.id.textViewSupplierFirstName);
        textViewSupplierName = findViewById(R.id.textViewSupplierName);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);


        // Asynchronous request
        Call<User> call = djangoRestApi.getUserByID(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    textViewSupplierFirstName.setText(response.body().getFirstName());
                    textViewSupplierName.setText(response.body().getName());
                } else {
                    Toast.makeText(getApplicationContext(), "An error occurred to retrieve the supplier info!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    public void fromOrderToMainActivity(View view) {

        // Change the is_available attribute of the product object to not available
        updateProduct(product);

        // Create the order object
        Order order = new Order(product.getSupplier(), product.getId());

        // Post order
        addOrder(order);

        // Redirect to the MainActivity
        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the OrderActivity

    }

    public void addOrder(Order order){
        /**
         * Take into param an order and add it to the remote database asynchronously
         *
         * @param Order order
         */

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Asynchronous request
        Call<Order> call = djangoRestApi.addOrder(order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "You order the product!", Toast.LENGTH_SHORT).show();

                } else {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    public void updateProduct(Product product){
        /**
         * Take into param a product and its id and update it in the remote database asynchronously
         *
         * @param Product product
         */

        int productId = product.getId();
        product.setIs_available(false);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Asynchronous request
        Call<Product> call = djangoRestApi.updateProduct(productId, product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(getApplicationContext(), "Not available", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }
    }
