package com.alrugaib.delivery.communication;

import com.alrugaib.delivery.model.Order;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.Url;

/**
 * This class is a holder for all requests using Retrofit, an interface for all endpoints
 */
public interface ApiService {

    /**
     * Api endpoint for retrieving order from passed invoice number
     *
     * @param invoiceNumber - invoice number of order
     * @return Order model assign to invoice number passed
     */
    @GET("/api/Orders/GetOrder")
    Call<Order> getOrder(@Query("invoiceNumber") String invoiceNumber);


    /**
     * Api endpoint to get time and durations from Google Directions API
     *
     * @param path - url address of google directions request
     * @return google directions response json
     */
    @GET
    Call<ResponseBody> getDirections(@Url String path);
}
