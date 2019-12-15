package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.model.Product;

public class OrderActivity extends AppCompatActivity {

    Button buttonOrder;
    TextView textViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        buttonOrder = findViewById(R.id.buttonOrder);
        textViewProduct = findViewById(R.id.textViewProduct);

        // Get class info in this new activity
        Intent toOrderActivityIntent = getIntent();
        Product product = (Product) toOrderActivityIntent.getSerializableExtra("productToOrder");

        // Insert product data in the textView
        textViewProduct.setText(product.getName());
    }
}