package com.alrugaibfurniture.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model of Customer Profile
 */
public class CustomerProfile implements Serializable {

    @SerializedName("PhoneNumber")
    String phoneNumber;
    @SerializedName("Email")
    String email;
    @SerializedName("FullName")
    String fullName;
    @SerializedName("ContactAddresses")
    ArrayList<Address> contactAddresses;

    /**
     * Get lists of addresses
     * @return
     */
    public ArrayList<Address> getDeliveryAddresses() {
        return contactAddresses;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContactAddresses(ArrayList<Address> contactAddresses) {
        this.contactAddresses = contactAddresses;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
