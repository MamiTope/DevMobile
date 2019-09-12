package com.mamitope.projects.devmobile.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mamitope.projects.devmobile.R;
import com.mamitope.projects.devmobile.payloads.ApiResponse;
import com.mamitope.projects.devmobile.payloads.UserPayload;
import com.mamitope.projects.devmobile.utils.ConnectivityManager;

public class SignUpActivity extends AppCompatActivity {

    private SignUpViewModel viewModel;

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;

    // for dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // init viewModel
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(R.string.title_sign_up);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signUp);

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
                    } else if (apiResponse.getMessage() != null) {
                        showToast(apiResponse.getMessage());
                        onBackPressed();
                        finish();
                    }
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
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check internet connection
                if (!ConnectivityManager.checkInternetConnection(SignUpActivity.this)) {
                    showToast(getString(R.string.internet));
                    return;
                }
                verifyInputs();
            }
        });
    }

    private void verifyInputs() {
        String usernameValue = username.getText() != null ? username.getText().toString() : "";
        String passwordValue = password.getText() != null ? password.getText().toString() : "";
        String confirmPasswordValue = confirmPassword.getText() != null ? confirmPassword.getText().toString() : "";

        // check input values
        if (usernameValue.isEmpty() || passwordValue.isEmpty() || confirmPasswordValue.isEmpty()) {
            showToast(getString(R.string.empty_fields));
            return;
        }

        // check passwords
        if (!passwordValue.equals(confirmPasswordValue)) {
            showToast(getString(R.string.different_passwords));
            return;
        }

        UserPayload payload = new UserPayload();
        payload.setUsername(usernameValue);
        payload.setPassword(passwordValue);
        payload.setRole("user");

        progressDialog.setMessage(getString(R.string.registration));
        viewModel.signUp(this, payload);
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
