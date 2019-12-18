package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frontend.CustomProductsAdapter;
import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Retrieve the lists of available products
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class CollectActivity extends AppCompatActivity {

    private ListView listViewAvailableProducts;
    private CustomProductsAdapter adapterAvailableProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        getAvailableProducts();
    }

    public void getAvailableProducts(){

        // Retrieve a reference on the listView defined in the xml file
        listViewAvailableProducts = findViewById(R.id.listViewAvailableProducts);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<List<Product>> callAvailableProducts = djangoRestApi.getAvailableProducts();
        // Asynchronous request
        callAvailableProducts.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("serverRequest",response.message());

                if (response.isSuccessful()) {

                    // Initialization of the productArrayList that will only contain available products
                    final ArrayList<Product> productArrayList = new ArrayList<>();
                    for (Product product : response.body()) {
                        if (product.getIs_available()) {
                            productArrayList.add(product);
                        }
                    }

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
                            // Send the product information to the OrderActivity
                            toOrderActivityIntent.putExtra("product", product);
                            startActivity(toOrderActivityIntent);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }
}