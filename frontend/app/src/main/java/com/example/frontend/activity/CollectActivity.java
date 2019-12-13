package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.CustomProductsAdapter;
import com.example.frontend.DjangoRestApi;
import com.example.frontend.R;
import com.example.frontend.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Possibility to retrieve the lists of available products
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class CollectActivity extends AppCompatActivity {

    private DjangoRestApi djangoRestApi;

    /**
     * Retrieves the available products in a listView thanks to the retrofit and GSon library
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        // Create a new class ?
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DjangoRestApi.baseUrl)
                // use of the GsonConverterFactory class to generate an implementation of the
                // DjangoRestApi interface which uses Gson for its deserialization.
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Generates an implementation of the DjangoRestApi
        djangoRestApi = retrofit.create(DjangoRestApi.class);

        getAllProducts();
    }

    private void getAllProducts() {

        Call<List<Product>> callAllProducts = djangoRestApi.getAllProducts();

        // Asynchronous request
        callAllProducts.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, final Response<List<Product>> response) {

                ListView listViewProducts;
                CustomProductsAdapter adapter;

                // Get the listView from the xml file
                listViewProducts = findViewById(R.id.listViewProducts);

                // Populate the products arrayList
                final ArrayList<Product> productsArrayList = (ArrayList<Product>) response.body();
                adapter = new CustomProductsAdapter(productsArrayList, getApplicationContext());

                listViewProducts.setAdapter(adapter);
                listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Product product = productsArrayList.get(position);
                        Toast.makeText(getApplicationContext(), product.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

                /*// Set a listener for item click
                listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent toOrderActivityIntent = new Intent();
                        toOrderActivityIntent.setClass(getApplicationContext(), OrderActivity.class);
                        toOrderActivityIntent.putExtra("product", response.body().get(position));
                        startActivity(toOrderActivityIntent);
                    }
                });*/



                // If the request fails:
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                }

               /* // Initialization of the list
                List<String> products = new ArrayList<>();
                // Print the product name in each item if the product isAvailable
                for (Product product : response.body()) {
                    if (product.getIs_available()) {
                        products.add(product.getName());
                    }
                }
                String[] arrayProducts = products.toArray(new String[products.size()]);
                ArrayAdapter<String> adapterProducts = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayProducts);
                listViewProducts.setAdapter(adapterProducts);
*/
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}