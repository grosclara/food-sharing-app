package com.example.cshare.Controllers.Activities;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import com.example.cshare.Controllers.Fragments.LoginFragment;
import com.example.cshare.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnButtonClickedListener {

    // FOR FRAGMENTS
    // Declare fragment
    private Fragment fragmentLogin;

    // FOR DATAS
    // Identify each fragment with a number
    private static final int FRAGMENT_LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Binding views
        ButterKnife.bind(this);

        // Show First Fragment
        this.showFragment(FRAGMENT_LOGIN);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show fragment according an Identifier
    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_LOGIN:
                this.showAddProductFragment();
                break;
            default:
                break;
        }
    }

    private void showAddProductFragment() {
        if (this.fragmentLogin == null) this.fragmentLogin = new LoginFragment();
        this.startTransactionFragment(this.fragmentLogin);
    }

    // Generic method that will replace and show a fragment inside the HomeActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            // Get our FragmentManager & FragmentTransaction (Inside an activity)
            // Use of SupportFragmentManager instead of FragmentManager to support
            // Android versions lower than 3.0
            getSupportFragmentManager().beginTransaction()
                    // Add it to FrameLayout container
                    .replace(R.id.activity_login_frame_layout, fragment).commit();
        }
    }

    // --------------
    // CallBack
    // --------------

    @Override
    public void onButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

    /*private LoginViewModel loginViewModel;

    @BindView(R.id.activity_login_username)
    EditText editTextUsername;
    @BindView(R.id.activity_login_password)
    EditText editTextPassword;
    @BindView(R.id.activity_login_login)
    Button buttonLogin;
    @BindView(R.id.activity_login_loading)
    ProgressBar progressBarLoading;*/


    /**
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        ButterKnife.bind(this);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                buttonLogin.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    editTextUsername.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    editTextPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                progressBarLoading.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(editTextUsername.getText().toString(),
                        editTextPassword.getText().toString());
            }
        };
        editTextUsername.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(editTextPassword.getText().toString(),
                            editTextPassword.getText().toString());
                }
                return false;
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.activity_login_login)
    protected void login(){
        progressBarLoading.setVisibility(View.VISIBLE);
        loginViewModel.login(editTextPassword.getText().toString(),
                editTextPassword.getText().toString());
    }
    **/
