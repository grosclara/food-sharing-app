package com.example.frontend.activity.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.frontend.R;
import com.example.frontend.activity.CartActivity;
import com.example.frontend.activity.CollectActivity;
import com.example.frontend.activity.MainActivity;
import com.example.frontend.adapter.CustomProductsAdapter;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Order;
import com.example.frontend.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Here we will handle the tab transactions
        View rootView;

        int index = getArguments().getInt(ARG_SECTION_NUMBER);

        switch (index) {
            case 1:
                rootView = inflater.inflate(R.layout.cart_tab1, container, false);
                getGivenProducts(CollectActivity.userId, rootView);
                return rootView;

            case 2:
                rootView = inflater.inflate(R.layout.cart_tab2, container, false);
                getCollectedProducts(CollectActivity.userId, rootView);
                return rootView;
        }
        return null;
    }

    /**
     * Send a HTTP request to retrieve all the products given by the user in the db in a ArrayList.
     * Then display them in a listView through the CustomProductAdapter
     * Set a onItemClickListener to the listView :
     *
     * @see CustomProductsAdapter
     */
    public void getGivenProducts(int userId, View view) {

        // Retrieve a reference on the listView defined in the xml file
        final ListView listViewGivenProducts = view.findViewById(R.id.listViewGivenProducts);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<List<Product>> callAvailableProducts = djangoRestApi.getGivenProducts(CollectActivity.token, CollectActivity.userId);

        // Asynchronous request
        callAvailableProducts.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("serverRequest", response.message());

                if (response.isSuccessful()) {

                    // Initialization of the productArrayList that will only contain available products
                    final ArrayList<Product> productArrayList = (ArrayList<Product>) response.body();

                    // Attach the adapter to the listView
                    CustomProductsAdapter adapterGivenProducts = new CustomProductsAdapter(productArrayList, getActivity().getApplicationContext());
                    listViewGivenProducts.setAdapter(adapterGivenProducts);

                    /*// The current object handles the event "click on a listView item"
                    listViewGivenProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    });*/
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    /**
     * Send a HTTP request to retrieve all the orders made by the user.
     * Then retrieve the product id in each order to store it in a list.
     * For each product id, send a HTTP request to retrieve the product data and to store it to an arrayList
     * Then display them in a listView through the CustomProductAdapter
     * Set a onItemClickListener to the listView :
     *
     * @see CustomProductsAdapter
     */
    public void getCollectedProducts(final int userId, final View view) {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        final DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<List<Order>> callClientOrders = djangoRestApi.getClientOrders(CollectActivity.token, userId);

        // Asynchronous request
        callClientOrders.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    // Initialization of the productIdArrayList that will contain ids of collected products
                    final ArrayList<Integer> productIdArrayList = new ArrayList<>();

                    // Fill the id list
                    for (Order order : response.body()) {
                        productIdArrayList.add(order.getProduct());
                    }

                    getProductsByIds(view, productIdArrayList);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.i("serverRequest", t.getLocalizedMessage());
            }
        });
    }

    private void getProductsByIds(View view, ArrayList<Integer> productIdArrayList) {

        // Retrieve a reference on the listView defined in the xml file
        final ListView listViewCollectedProducts = view.findViewById(R.id.listViewCollectedProducts);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<List<Product>> callClientOrders = djangoRestApi.getProductsByIds(CollectActivity.token, productIdArrayList);

        // Asynchronous request
        callClientOrders.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {

                    // Initialization of the productArrayList that will contain collected products
                    final ArrayList<Product> productArrayList = (ArrayList<Product>) response.body();

                    // Attach the adapter to the listView
                    CustomProductsAdapter adapterCollectedProducts = new CustomProductsAdapter(productArrayList,getActivity().getApplicationContext());
                    listViewCollectedProducts.setAdapter(adapterCollectedProducts);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("serverRequest", t.getLocalizedMessage());
            }
        });
    }
}