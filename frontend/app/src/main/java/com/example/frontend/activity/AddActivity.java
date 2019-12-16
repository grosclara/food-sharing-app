package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.R;
import com.example.frontend.api.RequestHelper;
import com.example.frontend.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Possibility to add a single product in the database by indicating its characteristics in a editView
 * After having added the product, the user is redirected to the MainActivity
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class AddActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private String productName;
    private RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    /**
     * Get the product information from the editTextViews to create a Product object.
     * Call the addProduct(Product) method
     * Eventually redirect to the MainActivity when clicking the buttonSubmit
     *
     * @param view buttonSubmit
     * @see #addProduct(Product product)
     */
    public void toMainActivity(View view) {

        // Retrieve the name of the product typed in the editText field
        editTextProductName = findViewById(R.id.editTextProductName);
        productName = String.valueOf(editTextProductName.getText());

        // Creation of a new product and its attribute
        Product product = new Product(productName, true, 3, "test");

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
         * Take into param a product and add it to the remote database asynchronously
         *
         * @param Product product
         */

        // Get a reference on the requestHelper object defined in MainActivity
        requestHelper = MainActivity.getRequestHelper();

        // Asynchronous request
        Call<Product> call = requestHelper.djangoRestApi.addProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.d("serverRequest", response.message());
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(getApplicationContext(), "Submit!", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(), "An error occured!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d("serverRequest", t.getMessage());
            }
        });
    }
}