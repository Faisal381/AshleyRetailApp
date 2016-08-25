package com.alrugaibfurniture.communication;


import com.alrugaibfurniture.BuildConfig;
import com.alrugaibfurniture.model.CustomerProfile;
import com.alrugaibfurniture.model.Order;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Retrofit Api Helper, singleton that wraps requests into methods
 */
public class ApiHelper {

    private static ApiHelper instance;
    private final ApiService apiService;
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
         * Create a service to make requests to the api
         */
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://furnituredeliverydemo.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        apiService = builder.create(ApiService.class);
    }

    /**
     * Singleton
     *
     * @return instance of ApiHelper
     */
    public static ApiHelper getInstance() {
        if (instance == null) {
            instance = new ApiHelper();
        }
        return instance;
    }


    /**
     * Send API request to check if number is in Database
     *
     * @param phoneNumber - phone number of customer
     * @param callback    callback to handle result
     */
    public void login(String phoneNumber, Callback<CustomerProfile> callback) {
        Call<CustomerProfile> call = apiService.login(phoneNumber);
        call.enqueue(callback);
    }

    /**
     * Submit request to API with all user information in Order model
     *
     * @param orderRequest model with all data of making order
     * @param callback     callback to handle result
     */
    public void makeOrder(Order orderRequest, Callback<Order> callback) {
        Call<Order> call = apiService.postOrder(orderRequest);
        call.enqueue(callback);
    }
}
