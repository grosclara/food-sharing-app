package com.example.frontend.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.activity.ui.main.OrderProductDialogFragment;
import com.example.frontend.adapter.CustomProductsAdapter;
import com.example.frontend.R;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Product;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * CollectActivity Class.
 * Displays the list of all available products in a listView.
 * Clicking on a product redirects the user to the OrderActivityT<;
 *
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

        token = "Token "+LauncherActivity.userCredits.getString("token", null).trim();
        userId = LauncherActivity.userCredits.getInt("id", -1);

        if (userId == -1 | token == null) {
            Log.e("Log in error", "Error while logging in for the first time");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(),"restart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"resume",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Call for the getAvailableProducts() in the onCreate method.
        getAvailableProducts();
        Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                fromMenuToSettingsActivity();
            case R.id.signOut:
                alertDialogLogOut();
                return true;
            case R.id.profile:
                fromMenuToProfileActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getAvailableProducts() {
        /**
         * Send a HTTP request to retrieve all the available products in the db in a ArrayList.
         * Then display them in a listView through the CustomProductAdapter
         * Set a onItemClickListener to the listView : clicking on an item, an alertDialog will pop up to access the product and the supplier information.
         *
         * @see CustomProductsAdapter
         */

        // Retrieve a reference on the listView defined in the xml file
        listViewAvailableProducts = findViewById(R.id.listViewAvailableProducts);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<List<Product>> callAvailableProducts = djangoRestApi.getAvailableProducts(token, 1);

        // Asynchronous request
        callAvailableProducts.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("serverRequest", response.message());

                if (response.isSuccessful()) {

                    // Initialization of the productArrayList that will only contain available products
                    final ArrayList<Product> productArrayList = (ArrayList<Product>) response.body();

                    // Attach the adapter to the listView
                    adapterAvailableProducts = new CustomProductsAdapter(productArrayList, getApplicationContext());
                    listViewAvailableProducts.setAdapter(adapterAvailableProducts);

                    // The current object handles the event "click on a listView item"
                    listViewAvailableProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Product product = productArrayList.get(position);

                            DialogFragment newFragment = new OrderProductDialogFragment(getApplicationContext(), product);
                            newFragment.show(getSupportFragmentManager(), "order");
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

    public void alertDialogLogOut() {
        // Alert Dialog to confirm the will to sign out

        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Are you sure you want to log out ?")
                .setTitle("Log out")
                // Add the buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button -> logout the user

                        Retrofit retrofit = NetworkClient.getRetrofitClient(getApplicationContext());
                        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

                        Call<ResponseBody> call = djangoRestApi.logout(token);
                        call.enqueue(new Callback<ResponseBody>() {


                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getApplicationContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();

                                LauncherActivity.userCreditsEditor.putBoolean("logStatus",false);
                                LauncherActivity.userCreditsEditor.apply();

                                Intent toSignInActivityIntent = new Intent();
                                toSignInActivityIntent.setClass(getApplicationContext(), SignInActivity.class);
                                startActivity(toSignInActivityIntent);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Fail to log out", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //finish();
                    }
                });
        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void fromCollectToAddActivity(View view) {
        /**
         * Redirect to the AddActivity when clicking the buttonCollect
         * @param View buttonAdd
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


    private void fromMenuToProfileActivity() {
        /**
         * Redirect to the ProfileActivity when clicking the item See Profile
         * @param item See Profile
         */
        Intent toProfileActivityIntent = new Intent();
        toProfileActivityIntent.setClass(getApplicationContext(), ProfileActivity.class);
        startActivity(toProfileActivityIntent);
    }

    private void fromMenuToSettingsActivity() {
    }



}