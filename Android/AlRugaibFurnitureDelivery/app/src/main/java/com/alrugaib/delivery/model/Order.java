package com.alrugaib.delivery.model;

import com.google.gson.annotations.SerializedName;


public class Order {

    @SerializedName("DeviceId")
    private String DeviceId;

    @SerializedName("DeliveryAddress")
    private Address deliveryAddress;

    @SerializedName("InvoiceNumber")
    private String invoiceNumber;


    public String getDeviceId() {
        return DeviceId;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

}