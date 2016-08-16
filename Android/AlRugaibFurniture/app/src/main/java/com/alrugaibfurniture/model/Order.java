package com.alrugaibfurniture.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model for making order and receiving response
 */
public class Order implements Serializable {

    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("DeliveryAddressNumber")
    private int deliveryAddressNumber;
    @SerializedName("DeliveryAddress")
    private Address deliveryAddress;
    @SerializedName("CustomerProfile")
    private CustomerProfile customerProfile;
    @SerializedName("InvoiceNumber")
    private String invoiceNumber;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeliveryAddressNumber() {
        return deliveryAddressNumber;
    }

    public void setDeliveryAddressNumber(int deliveryAddressNumber) {
        this.deliveryAddressNumber = deliveryAddressNumber;
    }

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
