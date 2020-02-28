package com.example.frontend.activity.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.frontend.R;
import com.example.frontend.activity.CollectActivity;
import com.example.frontend.api.DjangoRestApi;
import com.example.frontend.api.NetworkClient;
import com.example.frontend.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResetPasswordFragment extends DialogFragment {

    private EditText editTextEmail;

    private Context context;

    private String email;

    public ResetPasswordFragment(Context context) {
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
        View view = inflater.inflate(R.layout.dialog_reset_password, null);
        builder.setView(view);

        editTextEmail = view.findViewById(R.id.editTextEmail);

        builder.setTitle("Reset password")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Reset the password
                        email = editTextEmail.getText().toString().trim();
                        resetPassword();

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

    private void resetPassword() {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        User user = new User(email);
        // Creation of a call object that will contain the response
        Call<User> call = djangoRestApi.resetPassword(user);
        // Asynchronous request
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("serverRequest", response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Password reset e-mail has been sent.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "An error occurred while resetting password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
