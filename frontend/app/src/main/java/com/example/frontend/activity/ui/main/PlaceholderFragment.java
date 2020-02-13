package com.example.frontend.activity.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.frontend.R;
import com.example.frontend.activity.CartActivity;
import com.example.frontend.activity.CollectActivity;
import com.example.frontend.adapter.CustomProductsAdapter;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String state = "given";

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
            Log.d("TAG",String.valueOf(index));
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
                getGivenProducts(rootView);
                return rootView;

            case 2:
                rootView = inflater.inflate(R.layout.cart_tab2, container, false);
                getCollectedProducts(CollectActivity.userId, rootView);
                return rootView;
        }
        return null;
    }

    public void getGivenProducts(View view) {
        /**
         * Send a HTTP request to retrieve all the products given by the user in the db in a ArrayList.
         * Then display them in a listView through the CustomProductAdapter
         * Set a onItemClickListener to the listView :
         *
         * @see CustomProductsAdapter
         */

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

                    // The current object handles the event "click on a listView item"
                    listViewGivenProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Product product = productArrayList.get(position);

                            DialogFragment newFragment = new ProductDialogFragment(getContext(), product, state);
                            ((ProductDialogFragment) newFragment).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    getActivity().finish();
                                    getActivity().overridePendingTransition(0,0);
                                    startActivity(getActivity().getIntent());
                                    getActivity().overridePendingTransition(0,0);
                                }
                            });
                            newFragment.show(getChildFragmentManager(), state);
                        }
                    });

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

    public void getCollectedProducts(final int userId, final View view) {
        /**
         * Send a HTTP request to retrieve all the orders made by the user.
         * Then retrieve the product id in each order to store it in a list.
         * For each product id, send a HTTP request to retrieve the product data and to store it to an arrayList
         * Then display them in a listView through the CustomProductAdapter
         * Set a onItemClickListener to the listView :
         *
         * @see CustomProductsAdapter
         */

        // Retrieve a reference on the listView defined in the xml file
        final ListView listViewCollectedProducts = view.findViewById(R.id.listViewCollectedProducts);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        final DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<Object> call = djangoRestApi.getOrdersByClient(CollectActivity.token, userId);

        // Asynchronous request
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("serverRequest", response.message());

                if (response.isSuccessful()) {

                    final ArrayList<Product> collectedProducts = new ArrayList<>();

                    // Parsing the response.body() object
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    try {
                        JSONArray jsonArr = new JSONArray(json);
                        Log.e("TAG",jsonArr.toString());

                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            JSONObject jsonProduct = jsonObj.getJSONObject("product");
                            // Initialization of a product object to fill the collectedProducts ArrayList
                            Product product = new Product(jsonProduct.getInt("id"),
                                    jsonProduct.getString("name"),
                                    jsonProduct.getString("status"),
                                    jsonProduct.getString("created_at"),
                                    jsonProduct.getString("updated_at"),
                                    jsonProduct.getString("product_picture"),
                                    jsonProduct.getInt("supplier"),
                                    jsonProduct.getString("category"),
                                    jsonProduct.getString("quantity"),
                                    jsonProduct.getString("expiration_date"));

                            collectedProducts.add(product);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Attach the adapter to the listView
                    CustomProductsAdapter adapterCollectedProducts = new CustomProductsAdapter(collectedProducts, getActivity().getApplicationContext());
                    listViewCollectedProducts.setAdapter(adapterCollectedProducts);

                    listViewCollectedProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Product product = collectedProducts.get(position);

                            String state = product.getStatus().toLowerCase();

                            // When reloading the Activity, it will open by default on the Collected tab
                            Bundle bundle = new Bundle();
                            bundle.putInt(ARG_SECTION_NUMBER, 1);

                            DialogFragment newFragment = new ProductDialogFragment(getContext(), product, state);
                            newFragment.setArguments(bundle);
                            ((ProductDialogFragment) newFragment).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    getActivity().finish();
                                    getActivity().overridePendingTransition(0,0);
                                    startActivity(getActivity().getIntent());
                                    getActivity().overridePendingTransition(0,0);
                                }
                            });
                            newFragment.show(getChildFragmentManager(), state);


                        }
                    });

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("serverRequest", t.getLocalizedMessage());
            }
        });
    }
}