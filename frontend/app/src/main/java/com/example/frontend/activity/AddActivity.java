package com.example.frontend.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Product;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// PHOTOS PB

/**
 * AddActivity Class.
 * Give the possibility to the user to add a product in the database.
 * The form to fill the product includes EditTexts.
 * Allows to take a picture of the product by opening the camera clicking on the buttonPicture.
 * After having added the product, the user is redirected to the MainActivity.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class AddActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private ImageView imageViewProduct;

    private String productName;

    private Product product;

    // Ensures the intent to open the camera can be performed
    private static final int PICTURE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    /**
     * Get the product information from the editTextViews to create a Product object.
     * Call the addProduct(Product) method.
     * Eventually redirect to the MainActivity when clicking the buttonSubmit.
     * @param view buttonSubmit
     * @see #addProduct(Product product)
     */
    public void fromAddToMainActivity(View view) {

        // Retrieve the name of the product typed in the editText field
        editTextProductName = findViewById(R.id.editTextProductName);
        productName = String.valueOf(editTextProductName.getText());

        // Creation of a new product with its attribute
        // While the login module isn't set, we provide a default supplier id
        product = new Product(productName, 3);

        // Call for the addProduct(Product) method to transfer data to the server
        addProduct(product);

        // Go back to the mainActivity
        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the AddActivity
    }

    public void addProduct(Product product) {
        /**
         * Send a HTTP request to post the Product product taken in param
         * @param product
         */

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Asynchronous request
        Call<Product> call = djangoRestApi.addProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(getApplicationContext(), "Submit!", Toast.LENGTH_SHORT);
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