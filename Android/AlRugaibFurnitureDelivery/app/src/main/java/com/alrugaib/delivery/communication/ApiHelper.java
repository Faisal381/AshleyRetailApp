package com.alrugaib.delivery.communication;


import com.alrugaib.delivery.BuildConfig;
import com.alrugaib.delivery.model.Order;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
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
         * Create a service to make API requests
         */
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://furnituredeliverydemo.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        apiService = builder.create(ApiService.class);
        /*
         * Create a service to make Google API requests
         */
        builder = new Retrofit.Builder()
                .baseUrl("http://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        googleService = builder.create(ApiService.class);
    }

    /**
     * Singleton
     */
    public static ApiHelper getInstance() {
        if (instance == null) {
            instance = new ApiHelper();
        }
        return instance;
    }

    public void getOrder(String invoiceNumber, Callback<Order> callback) {
        Call<Order> call = apiService.getOrder(invoiceNumber);
        call.enqueue(callback);
    }

    /**
     * Get Google Directions from url with waypoints
     * @param url
     * @param callback
     */
    public void getDirections(String url, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = googleService.getDirections(url);
        call.enqueue(callback);
    }
}
