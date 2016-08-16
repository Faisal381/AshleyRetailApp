package com.alrugaibfurniture.communication;

import com.alrugaibfurniture.model.CustomerProfile;
import com.alrugaibfurniture.model.Order;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This class is a holder for all requests using Retrofit
 */

public interface ApiService {

    @GET("/api/CustomerProfiles/GetCustomerProfile")
    Call<CustomerProfile> login(@Query("phone") String phoneNumber);


    @POST("api/Orders/PostOrderWithProfileUpdate")
    Call<Order> postOrder(@Body Order model);
}
