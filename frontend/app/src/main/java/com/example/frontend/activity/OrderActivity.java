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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Possibility to retrieve the information of both the product and the supplier of the product we want to order.
 * Clicking on the buttonOrder, the status of the product is set to non available and a order object is created and post to the remote db
 * After having ordered the product, the user is redirected to the MainActivity
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */
public class OrderActivity extends AppCompatActivity {

    private TextView textViewProductName;
    private TextView textViewProductStatus;
    private TextView textViewSupplierName;
    private TextView textViewSupplierFirstName;
    private ImageView imageViewOrderProduct;

    private Product product;

    /**
     * Retrieve the object product (Product) by a getExtra to the intent sent by the collectActivity
     * Display the information of both the product and the supplier from the product variable and the getUserById method
     * @param savedInstanceState
     * @see #getUserById(int)
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
        imageViewOrderProduct = findViewById(R.id.imageViewOrderProduct);

        // Display the product info in the xml file
        textViewProductName.setText(product.getName());
        // Check to see if the product is still available
        if(product.getIs_available()){
            textViewProductStatus.setText("Available");
        } else{
            textViewProductStatus.setText("Already ordered by someone else");
        }
        Picasso.get().load(product.getProduct_picture()).into(imageViewOrderProduct);
    }

    /**
     * Send a HTTP request to retrieve all information from the user
     * @param userId
     */
    public void getUserById(int userId){

        // Retrieve a reference on the textViews defined in the xml layout file
        textViewSupplierFirstName = findViewById(R.id.textViewSupplierFirstName);
        textViewSupplierName = findViewById(R.id.textViewSupplierName);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.getUserByID(userId);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    textViewSupplierFirstName.setText(response.body().getFirst_name());
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

    /**
     * When clicking the Button buttonOrder:
     * Call the method to update the product (updateProduct) in the remote db
     * Create an order object to post to the remote db calling the addOrder method
     * Redirect to the Main when clicking the buttonOrder
     * @param view buttonOrder
     * @see #addOrder(Order)
     * @see #updateProduct(Product)
     */
    public void fromOrderToMainActivity(View view) {
        // Create the order object
        Order order = new Order(MainActivity.userId, product.getId());
        // Post order
        addOrder(order);
        // Change the is_available attribute of the product object to not available
        updateProduct(product);
        // Redirect to the MainActivity
        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the OrderActivity

    }

    /**
     * Take into param an order and add it to the remote database asynchronously
     * @param order
     */
    public void addOrder(Order order){

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<Order> call = djangoRestApi.addOrder(order);
        // Asynchronous request
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

    // PB WITH THE PICTURE FIELD
    /**
     * Take into param a product and update it in the remote database asynchronously
     * @param product
     */
    public void updateProduct(Product product){

        // Retrieve the id of the product
        int productId = product.getId();
        // Set attributes to null so that they are not changed in the db by the HTTP PATCH request
        product.setProduct_picture(null);
        product.setCreated_at(null);
        product.setName(null);
        // Set its is_available attribute to false as it has just been order by someone
        product.setIs_available(false);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<Product> call = djangoRestApi.updateProduct(productId, product);
        // Asynchronous request
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
