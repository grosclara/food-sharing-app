package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollectActivity extends AppCompatActivity {

    private TextView textViewUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        textViewUserList = findViewById(R.id.textViewUserList);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        Call<List<User>> callAllUsers =djangoRestApi.getAllUsers();

        callAllUsers.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    textViewUserList.setText("Code HTTP: "+response.code());
                }

                textViewUserList.setText("Code HTTP: "+response.code());

               // List<User> users = response.body();

                //for(User user : users){
                  //  String content="";
                    //content += "Name: " + user.getName() + "\n";
                    //content += "First name: " + user.getFirstName() + "\n";
                    //textViewUserList.append(content);
                //}
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewUserList.setText(t.getMessage());
                Log.d("connection", "failed to connect to localhost");
            }
        });
    }
}
