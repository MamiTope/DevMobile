package com.mamitope.projects.devmobile.activities;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.mamitope.projects.devmobile.R;
import com.mamitope.projects.devmobile.payloads.ApiResponse;
import com.mamitope.projects.devmobile.payloads.UserPayload;
import com.mamitope.projects.devmobile.retrofit.interfaces.ApiClient;
import com.mamitope.projects.devmobile.retrofit.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {

    private MutableLiveData<Boolean> loading;
    private MutableLiveData<ApiResponse> apiResponse;

    public LiveData<Boolean> isLoading() {
        if (loading == null)
            loading = new MutableLiveData<>();

        loading.setValue(false);
        return loading;
    }

    public LiveData<ApiResponse> getApiResponse() {
        if (apiResponse == null)
            apiResponse = new MutableLiveData<>();

        return apiResponse;
    }

    public void signUp(final Context context, UserPayload payload) {
        loading.setValue(true);

        ApiInterface api = ApiClient.getClient(context).create(ApiInterface.class);

        Call<ApiResponse> call = api.signUp(payload);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loading.setValue(false);

                if (response.isSuccessful()) {
                    apiResponse.setValue(response.body());
                } else {
                    try {
                        ApiResponse error = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        apiResponse.setValue(error);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                loading.setValue(false);
                Toast.makeText(context, context.getString(R.string.internet), Toast.LENGTH_LONG).show();
            }
        });
    }
}
