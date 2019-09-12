package com.mamitope.projects.devmobile.retrofit.interfaces;

import com.mamitope.projects.devmobile.payloads.ApiResponse;
import com.mamitope.projects.devmobile.payloads.BookResponse;
import com.mamitope.projects.devmobile.payloads.UserPayload;
import com.mamitope.projects.devmobile.payloads.SignInResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    String BASE_URL = "https://dev-mobile-api.herokuapp.com/api/v1/";

    @POST("auth/signin")
    Call<SignInResponse> signIn(@Body UserPayload payload);

    @POST("auth/signup")
    Call<ApiResponse> signUp(@Body UserPayload payload);

    @GET("books")
    Call<BookResponse> getBooks();
}
