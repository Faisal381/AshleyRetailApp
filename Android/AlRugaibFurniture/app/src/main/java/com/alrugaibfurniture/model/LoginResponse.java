package com.alrugaibfurniture.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class LoginResponse implements Serializable {

    @SerializedName("Id")
    int id;
    @SerializedName("PhoneNumber")
    String phoneNumber;
    @SerializedName("Email")
    String email;
    @SerializedName("FullName")
    String fullName;
    @SerializedName("DeliveryAddresses")
    ArrayList<Address> deliveryAddresses;

    public ArrayList<Address> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
