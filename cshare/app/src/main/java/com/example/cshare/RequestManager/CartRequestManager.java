package com.example.cshare.RequestManager;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Product;
import com.example.cshare.WebServices.NetworkClient;
import java.util.List;
import retrofit2.Retrofit;

/**
 * CartRequestManager is a class that sends api calls and holds cart data as attributes
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class CartRequestManager {
    private MutableLiveData<List<Product>> cartLiveData;
    private static CartRequestManager cartRepository;

    private Retrofit retrofit ;

    public CartRequestManager(){
        /**
         * Constructor that fetch all the list of collected products and store it in the
         * products attributes
         */

        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        //djangoRestApi = retrofit.create(DjangoRestApi.class);

       // cartLiveData = getCartProducts("token 11d882f91e4bf9b410287932404186a7919c4ec1", 5);
    }
    public MutableLiveData<List<Product>> getProducts() {
        return cartLiveData;
    }

    public synchronized static CartRequestManager getInstance() {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (cartRepository == null) {
            if (cartRepository == null) {
                cartRepository = new CartRequestManager();
            }
        }
        return cartRepository;
    }

/*
    public MutableLiveData<List<Product>> getCartProducts(String token, int userId){*/
        /**
         * request to the API to get cart products list
         */
/*
        //creation of cartProducts live data
        MutableLiveData<List<Product>> cartProducts = new MutableLiveData<>();
        // Creation of a call object that will contain the response
        //Call<Object> callAvailableProducts = djangoRestApi.getOrdersByClient(token, userId);
        // Asynchronous request
        callAvailableProducts.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("serverRequest", response.message());

                if (response.isSuccessful()) {

                    final List<Product> productsList = new ArrayList<>();

                    // Parsing the response.body() object
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    try {
                        JSONArray jsonArr = new JSONArray(json);
                        Log.e("TAG", jsonArr.toString());

                        for (int i = 0; i < jsonArr.length(); i++) {
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

                            productsList.add(product);
                            // set response in productList live data


                        }
                        cartProducts.setValue(productsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                cartProducts.setValue(null);
            }
        });
        return cartProducts;
    }
    */
}
