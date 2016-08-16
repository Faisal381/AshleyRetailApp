package com.alrugaib.delivery.model;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model of Address as part of Order model
 */
public class Address implements Serializable {

    @SerializedName("Id")
    int id;
    @SerializedName("Name")
    String name;
    @SerializedName("Lat")
    double lat;
    @SerializedName("Lon")
    double lon;

    public Address(int id, String name, double lat, double lon){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
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
        return name;
    }

    public LatLng getLocation(){
        return new LatLng(lat,lon);
    }
}
