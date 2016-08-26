package com.alrugaibfurniture.communication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Error model on HTTP status 400
 */
public class ErrorResponse implements Serializable {
    @SerializedName("Message")
    String Message;

    public String getMessage(){
        return Message;
    }
}
