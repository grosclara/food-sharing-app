package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.frontend.R;
import com.example.frontend.api.RequestHelper;

/**
 * Launcher activity that allows navigating to the Collect and Add activities
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    private  static RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestHelper = new RequestHelper();
    }

    public static RequestHelper getRequestHelper() {
        return requestHelper;
    }

    public void toCollectActivity(View view) {
        /**
         * Redirect to the CollectActivity when clicking the buttonCollect
         * @param Button buttonCollect
         */
        Intent toCollectActivityIntent = new Intent();
        toCollectActivityIntent.setClass(getApplicationContext(), CollectActivity.class);
        startActivity(toCollectActivityIntent);
    }

    public void toAddActivity(View view) {
        /**
         * Redirect to the AddActivity when clicking the buttonCollect
         */
        Intent toAddActivityIntent = new Intent();
        toAddActivityIntent.setClass(getApplicationContext(), AddActivity.class);
        startActivity(toAddActivityIntent);
    }
}
