package com.mamitope.projects.devmobile.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mamitope.projects.devmobile.R;
import com.mamitope.projects.devmobile.database.SessionManager;
import com.mamitope.projects.devmobile.payloads.ApiResponse;
import com.mamitope.projects.devmobile.payloads.SignInResponse;
import com.mamitope.projects.devmobile.payloads.UserPayload;
import com.mamitope.projects.devmobile.utils.ConnectivityManager;

public class SignInActivity extends AppCompatActivity {

    private SignInViewModel viewModel;
    private SessionManager sessionManager;

    private EditText username;
    private EditText password;
    private Button signIn;
    private TextView signInAction;

    // for dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        viewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        sessionManager = SessionManager.getInstance(getApplicationContext());

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        signInAction = findViewById(R.id.signUpAction);

        // initializations
        initProgressDialog();
        initActions();

        // binding view models
        viewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        progressDialog.show();
                    } else {
                        progressDialog.dismiss();
                    }
                }
            }
        });

        viewModel.getApiResponse().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                if (apiResponse != null) {
                    if (apiResponse.getError() != null) {
                        showToast(apiResponse.getError());
                    }
                }
            }
        });

        viewModel.getSignInResponse().observe(this, new Observer<SignInResponse>() {
            @Override
            public void onChanged(SignInResponse signInResponse) {
                if (signInResponse != null) {
                    sessionManager.signIn(signInResponse.getToken());
                    sessionManager.setProfile(signInResponse.getUser());
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(getString(R.string.internet));
                }
            }
        });
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
    }


    private void initActions() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check internet connection
                if (!ConnectivityManager.checkInternetConnection(SignInActivity.this)) {
                    showToast(getString(R.string.internet));
                    return;
                }
                verifyInputs();
            }
        });

        signInAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verifyInputs() {
        String usernameValue = username.getText() != null ? username.getText().toString() : "";
        String passwordValue = password.getText() != null ? password.getText().toString() : "";

        // check input values
        if (usernameValue.isEmpty() || passwordValue.isEmpty()) {
            showToast(getString(R.string.empty_fields));
            return;
        }

        UserPayload payload = new UserPayload();
        payload.setUsername(usernameValue);
        payload.setPassword(passwordValue);

        progressDialog.setMessage(getString(R.string.connection));
        viewModel.signIn(this, payload);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
