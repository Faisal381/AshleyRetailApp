package com.alrugaib.delivery.communication;


import android.util.Log;

import com.alrugaib.delivery.BuildConfig;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class ApiHelper {

    private static ApiHelper instance;
    private final ApiService apiService;
    private final ApiService googleService;
    private static final int TIMEOUT = 10;//sec

    private ApiHelper() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            // if debug build then add verbose logging
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(logging);
        }

        /*
         * Create a service to make authed requests to the api
         */
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        apiService = builder.create(ApiService.class);

        builder = new Retrofit.Builder()
                .baseUrl("http://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        googleService = builder.create(ApiService.class);
    }

    public static ApiHelper getInstance() {
        if (instance == null) {
            instance = new ApiHelper();
        }
        return instance;
    }


    /**
     * Send API request to check if number is in Database
     *
     * @param phoneNumber
     */
    public void getLogin(long phoneNumber) {
        Call<ResponseBody> call = apiService.getTest();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void getDirections(String url, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = apiService.getDirections(url);
        call.enqueue(callback);
    }
}
