package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frontend.adapter.CustomProductsAdapter;
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
 * CollectActivity Class.
 * Displays the list of all available products in a listView.
 * Clicking on a product redirects the user to the OrderActivityT<;
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class CollectActivity extends AppCompatActivity {

    private ListView listViewAvailableProducts;
    private CustomProductsAdapter adapterAvailableProducts;

    public static String token;
    public static int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        Toast.makeText(getApplicationContext(),String.valueOf(userId),Toast.LENGTH_SHORT).show();

        token = LauncherActivity.userCredits.getString("token",null);
        userId = LauncherActivity.userCredits.getInt("id",-1);

        if( userId==-1 | token == null){
            Log.e("Log in error","Error while logging in for the first time");
        }

        // Call for the getAvailableProducts() in the onCreate method.
        getAvailableProducts();
    }

    /**
     * Send a HTTP request to retrieve all the available products in the db in a ArrayList.
     * Then display them in a listView through the CustomProductAdapter
     * Set a onItemClickListener to the listView : clicking on an item, the user will be redirected to the OrderActivity and the intent will contain the product information.
     * @see CustomProductsAdapter
     */
    public void getAvailableProducts(){

        // Retrieve a reference on the listView defined in the xml file
        listViewAvailableProducts = findViewById(R.id.listViewAvailableProducts);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<List<Product>> callAvailableProducts = djangoRestApi.getAvailableProducts(token,1);

        // Asynchronous request
        callAvailableProducts.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("serverRequest",response.message());

                if (response.isSuccessful()) {

                    // Initialization of the productArrayList that will only contain available products
                    final ArrayList<Product> productArrayList = (ArrayList<Product>) response.body();

                    // Attach the adapter to the listView
                    adapterAvailableProducts = new CustomProductsAdapter(productArrayList,getApplicationContext());
                    listViewAvailableProducts.setAdapter(adapterAvailableProducts);

                    // The current object handles the event "click on a listView item"
                    listViewAvailableProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Product product = productArrayList.get(position);

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

    public void fromCollectToAddActivity(View view) {
        /**
         * Redirect to the AddActivity when clicking the buttonCollect
         * @param Button buttonAdd
         */
        Intent toAddActivityIntent = new Intent();
        toAddActivityIntent.setClass(getApplicationContext(), AddActivity.class);
        startActivity(toAddActivityIntent);
    }

    public void fromCollectToCartActivity(View view) {
        /**
         * Method attached to the Shopping Cart button that redirects to the CartActivity when clicking the button
         * @param Button buttonCart
         */
        Intent toCartActivityIntent = new Intent();
        toCartActivityIntent.setClass(getApplicationContext(), CartActivity.class);
        startActivity(toCartActivityIntent);
    }
}