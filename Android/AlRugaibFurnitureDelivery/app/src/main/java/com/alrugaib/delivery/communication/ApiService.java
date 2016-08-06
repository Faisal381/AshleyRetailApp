package com.alrugaib.delivery.communication;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;


public interface ApiService {


    @GET("/test")
    Call<ResponseBody> getTest();


    @GET
    Call<ResponseBody> getDirections(@Url String path);
}
