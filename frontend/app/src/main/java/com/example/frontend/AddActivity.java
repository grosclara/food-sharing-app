package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    public void toMainActivity(View view) {
        /**
         * Call for the postProduct() method and then redirect to the MainActivity when clicking the buttonValidate
         * @see #postProduct()
         * @param Button buttonValidate
         */

        editTextProductName = findViewById(R.id.editTextProductName);
        productName = String.valueOf(editTextProductName.getText());

        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(toMainActivityIntent);
        finish(); // Disable the "going back functionality" from the MainActivity to the AddActivity
    }
}
