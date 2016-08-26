package com.alrugaibfurniture.communication;

import com.alrugaibfurniture.model.CustomerProfile;
import com.alrugaibfurniture.model.Order;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This class is a holder for all requests using Retrofit, an interface for all endpoints
 */
public interface ApiService {

    /**
     * Api endpoint for customer profile after user input phone number
     *
     * @param phoneNumber - phone number of customer
     * @return profile of existing user (if he didn't exist, backend create new user from phone number)
     */
    @GET("/api/CustomerProfiles/GetCustomerProfile")
    Call<CustomerProfile> login(@Query("phone") String phoneNumber);

    /**
     * Api endpoint for posting customer order
     *
     * @param model - order model
     * @return order model to validate in debug mode
     */
    @POST("/api/Orders/PostOrderWithProfileUpdate")
    Call<Order> postOrder(@Body Order model);
}
