package com.alrugaibfurniture.communication;

import com.alrugaibfurniture.model.LoginResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * This class is a holder for all requests using Retrofit
 */

public interface ApiService {

    @GET("/api/CustomerProfiles/GetCustomerProfile")
    Call<LoginResponse> login(@Query("phone") String phoneNumber);

}
