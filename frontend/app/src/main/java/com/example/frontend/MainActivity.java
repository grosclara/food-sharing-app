package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toCollectActivity(View view) {
        Intent toCollectActivityIntent = new Intent();
        toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
        startActivity(toCollectActivityIntent);
    }

    public void toAddActivity(View view) {
        Intent toAddActivityIntent = new Intent();
        toAddActivityIntent.setClass(getApplicationContext(), AddActivity.class);
        startActivity(toAddActivityIntent);
    }
}
