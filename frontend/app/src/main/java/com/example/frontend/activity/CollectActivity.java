package com.example.frontend.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frontend.CustomProductsAdapter;
import com.example.frontend.api.GetAvailableProductsCallbacks;
import com.example.frontend.R;
import com.example.frontend.api.RequestHelper;
import com.example.frontend.model.Product;

import java.util.ArrayList;

/**
 * Possibility to retrieve the lists of available products
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class CollectActivity extends AppCompatActivity {

    private RequestHelper requestHelper;
    private ListView listViewAvailableProducts;
    private CustomProductsAdapter adapterAvailableProducts;

    /**
     * Call fro the getAvailableProducts() method to retrieves the available products in a listView
     * @see RequestHelper#addProduct(Product)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        listViewAvailableProducts = findViewById(R.id.listViewAvailableProducts);

        // Get a reference on the requestHelper object defined in MainActivity
        requestHelper = MainActivity.getRequestHelper();

        // Retrieve the arrayList of available products to process the data
        requestHelper.getAvailableProducts(new GetAvailableProductsCallbacks() {
            @Override
            public void onSuccess(@NonNull final ArrayList<Product> productArrayList) {

                // Attach the adapter to the listView
                adapterAvailableProducts = new CustomProductsAdapter(productArrayList,getApplicationContext());
                listViewAvailableProducts.setAdapter(adapterAvailableProducts);

                // The current object handles the event "click on a listView item"
                listViewAvailableProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Product product = productArrayList.get(position);

                        // Toast the name of the product
                        Toast.makeText(getApplicationContext(), product.getName(), Toast.LENGTH_SHORT).show();

                        // Redirect to the OrderActivity
                        Intent toOrderActivityIntent = new Intent();
                        toOrderActivityIntent.setClass(getApplicationContext(), OrderActivity.class);
                        toOrderActivityIntent.putExtra("productToOrder", product);
                        startActivity(toOrderActivityIntent);
                    }
                });
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.d("error","getAvailableProductRequestError");
            }
        });
    }
}