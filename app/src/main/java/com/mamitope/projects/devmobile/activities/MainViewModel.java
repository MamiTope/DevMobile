package com.mamitope.projects.devmobile.activities;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.mamitope.projects.devmobile.payloads.ApiResponse;
import com.mamitope.projects.devmobile.payloads.BookResponse;
import com.mamitope.projects.devmobile.retrofit.interfaces.ApiClient;
import com.mamitope.projects.devmobile.retrofit.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> loading;
    private MutableLiveData<ApiResponse> apiResponse;
    private MutableLiveData<BookResponse> bookResponse;

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

    public LiveData<BookResponse> getBookResponse() {
        if (bookResponse == null)
            bookResponse = new MutableLiveData<>();

        return bookResponse;
    }

    public void loadBooks(Context context) {
        loading.setValue(true);
        ApiInterface api = ApiClient.getClient(context).create(ApiInterface.class);

        Call<BookResponse> call = api.getBooks();
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    bookResponse.setValue(response.body());
                } else {
                    try {
                        ApiResponse error = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        apiResponse.setValue(error);
                    } catch (Exception e) {
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}
