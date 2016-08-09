package com.alrugaibfurniture.communication;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * This class is a holder for all requests using Retrofit
 */

public interface ApiService {

    @GET("/api/CustomerProfiles/GetCustomerProfile?phone={phone}")
    Call<ResponseBody> login(@Path("phone") String phoneNumber);

}
