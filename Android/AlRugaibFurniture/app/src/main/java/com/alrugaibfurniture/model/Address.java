package com.alrugaibfurniture.model;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address  implements Serializable {

    @SerializedName("Id")
    int id;
    @SerializedName("CustomerProfileId")
    int customerProfileId;
    @SerializedName("Name")
    String Name;
    @SerializedName("Lat")
    double lat;
    @SerializedName("Lon")
    double lon;

    public int getCustomerProfileId() {
        return customerProfileId;
    }

    public int getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return Name;
    }

    public LatLng getLocation(){
        return new LatLng(lat,lon);
    }
}
