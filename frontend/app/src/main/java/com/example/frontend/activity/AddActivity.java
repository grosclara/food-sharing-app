package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.frontend.R;
import com.example.frontend.api.RequestHelper;
import com.example.frontend.model.Product;

/**
 * Possibility to add a single product in the database by indicating its characteristics
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
     * Call for the addProduct(Product) method and then redirect to the MainActivity when clicking the buttonSubmit
     *
     * @param view buttonSubmit
     * @see com.example.frontend.api.RequestHelper#addProduct(Product)
     */
    public void toMainActivity(View view) {

        // Retrieve the name of the product typed in the editText field
        editTextProductName = findViewById(R.id.editTextProductName);
        productName = String.valueOf(editTextProductName.getText());

        // Creation of a new product and its attribute
        Product product = new Product(productName, true, 3, "test");

        // Get a reference on the requestHelper object defined in MainActivity
        requestHelper = MainActivity.getRequestHelper();
        requestHelper.addProduct(product);

        // Go back to the mainActivity
        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the AddActivity
    }
}
