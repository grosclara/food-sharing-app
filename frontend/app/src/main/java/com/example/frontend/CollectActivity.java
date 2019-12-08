package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollectActivity extends AppCompatActivity {

    private TextView textViewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        Call<List<Product>> callAllProducts = djangoRestApi.getAllProducts();

        callAllProducts.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                textViewProducts = findViewById(R.id.textViewProducts);

                if (!response.isSuccessful()) {
                    textViewProducts.setText("Code HTTP: " + response.code());
                }

                List<Product> products = response.body();

                for (Product product : products) {
                    if (product.getIsAvailable()) {
                        String content = "";
                        content += "ProductID: " + product.getId() + "\n";
                        content += "Name: " + product.getName() + "\n";
                        content += "Offerer: " + product.getOfferer() + "\n\n";
                        textViewProducts.append(content);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("connection", "failed to connect to localhost");
            }
        });
    }
}
