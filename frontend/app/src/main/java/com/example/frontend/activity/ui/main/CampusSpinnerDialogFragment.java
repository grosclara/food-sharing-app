package com.example.frontend.activity.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.frontend.R;
import com.example.frontend.activity.CollectActivity;
import com.example.frontend.activity.LauncherActivity;
import com.example.frontend.activity.SignInActivity;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.example.frontend.model.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CampusSpinnerDialogFragment extends DialogFragment {

    private Context context;

    private Spinner spinnerCampus;
    private String[] campusArray;
    private String campus;

    public CampusSpinnerDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_spinner_campus, null);
        builder.setView(view);

        // Spinner
        spinnerCampus = view.findViewById(R.id.spinnerCampus);
        campusArray = getResources().getStringArray(R.array.campus_array);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, campusArray);

        // Apply the adapter to the spinner
        spinnerCampus.setAdapter(adapterSpinner);
        spinnerCampus.setSelection(adapterSpinner.getPosition(CollectActivity.campus));
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                campus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setTitle("Select your campus")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        changeCampus();
                        //Do some code
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void changeCampus() {
        // PATCH request to change the status of the user and redirected to the sign in activity so that the cmapus can be updated in the shared pref
        Map<String, String> campusMap = new HashMap<>();
        campusMap.put("campus", campus);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.changeUserCampus(CollectActivity.token, CollectActivity.userId, campusMap);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    logOut();
                } else {
                    Toast.makeText(context, "An error occurred!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("serverRequest", t.getMessage());
            }
        });
    }

    private void logOut(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        Call<ResponseBody> call = djangoRestApi.logout(CollectActivity.token);
        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT).show();

                LauncherActivity.userCreditsEditor.putBoolean("logStatus",false);
                LauncherActivity.userCreditsEditor.apply();

                Intent toSignInActivityIntent = new Intent();
                toSignInActivityIntent.setClass(context, SignInActivity.class);
                startActivity(toSignInActivityIntent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Fail to log out", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
