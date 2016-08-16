package com.alrugaib.delivery.communication;

import com.alrugaib.delivery.model.Order;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;

/**
 * This class is a holder for all requests using Retrofit
 */

public interface ApiService {


    @GET("/api/Orders/GetOrder")
    Call<Order> getOrder(@Query("invoiceNumber") String invoiceNumber);


    @GET
    Call<ResponseBody> getDirections(@Url String path);
}
