package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private EditText editTextProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void toMainActivity(View view) {

        editTextProductName = findViewById(R.id.editTextProductName);
        String productName = String.valueOf(editTextProductName.getText());

        Intent toMainActivityIntent = new Intent();
        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);

        Toast.makeText(getApplicationContext(),productName, Toast.LENGTH_SHORT).show();
        startActivity(toMainActivityIntent);
        finish();
    }
}
