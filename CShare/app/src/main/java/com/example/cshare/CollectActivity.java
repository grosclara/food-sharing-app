package com.example.cshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class CollectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Log.d("cycle","onCreate");
    }

    private void GetAllProducts() {

        String url_api = "https:/api.chucknorris.io/jokes/random"; //à changer avec l'api Django

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Response.Listener<String> responseListener = new GetAllProductsResponseListener();
        Response.ErrorListener responseErrorListener = new GetALlProductsResponseErrorListener();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_api, responseListener, responseErrorListener);
        requestQueue.add(stringRequest);
        Log.d("get","GetALlProducts");
    }

    private class GetAllProductsResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            TextView textViewProduct = findViewById(R.id.textViewProduct);
            String msg = "";
            try {
                JSONObject jsonObject = new JSONObject(response);
                msg = jsonObject.getString("value");
            } catch (Exception e) {
                msg = "Problème pour parser le produit";
            }
            textViewProduct.setText(msg);
        }
    }

    private class GetALlProductsResponseErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            TextView textViewProduct = findViewById(R.id.textViewProduct);
            textViewProduct.setText("Echec de récupération du produit");
        }
    }
}
