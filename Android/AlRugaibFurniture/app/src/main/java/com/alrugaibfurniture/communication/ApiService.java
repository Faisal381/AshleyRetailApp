package com.alrugaibfurniture.communication;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

/**
 * This class is a holder for all requests using Retrofit
 */

public interface ApiService {


    @GET("/test")
    Call<ResponseBody> getTest();


}
