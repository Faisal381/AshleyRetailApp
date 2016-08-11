package com.alrugaib.delivery.communication;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Url;

/**
 * Interface for Retrofit
 */
public interface ApiService {


    @GET("/api/CustomerProfiles/GetCustomerProfile?phone={phone}")
    Call<ResponseBody> login(@Path("phone") String phoneNumber);


    @GET
    Call<ResponseBody> getDirections(@Url String path);
}
