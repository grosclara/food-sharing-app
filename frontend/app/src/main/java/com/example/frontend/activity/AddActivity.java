package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.DjangoRestApi;
import com.example.frontend.R;
import com.example.frontend.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Possibility to add a single product in the database by indicating its characteristics
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class AddActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    /**
     * Call for the createProduct() method and then redirect to the MainActivity when clicking the buttonSubmit
     * @see #createProduct()
     * @param view buttonSubmit
     */
    public void toMainActivity(View view) {

        createProduct();

        // Go back to the mainActivity
        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the AddActivity
    }

    /**
     * Post request retrieving the text typed in the editText field
     */
    private void createProduct()    {

        // CReation of the retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        editTextProductName = findViewById(R.id.editTextProductName);
        // Retrieve the name of the product typed in the editText field
        productName = String.valueOf(editTextProductName.getText());

        // Creation of a new product and its attribute
        Product product = new Product(productName, true, 3);

        // Asynchronous request
        Call<Product> call = djangoRestApi.createProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                // In case of success, toast "Submit!"
                Toast.makeText(getApplicationContext(), "Submit!", Toast.LENGTH_SHORT);
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) { }
        });
    }
}
