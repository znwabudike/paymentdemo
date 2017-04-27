package com.drawingboardapps.transactionsdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Transaction result POJO
 * Created by Zach on 4/23/2017.
 */

public class TransactionResult extends RealmObject implements Serializable {

    public static String USER_CANCELLED = "user_cancelled";
    //    @SerializedName("transID")
//    @Expose
    private String transID = "";
    //    @SerializedName("timestamp")
//    @Expose
    private String timestamp = "";
    //    @SerializedName("amount")
//    @Expose
    private String amount = "";
    //    @SerializedName("response")
//    @Expose
    private String response = "";
    //    @SerializedName("responseCode")
//    @Expose
    private String responseCode = "";


    /**
     * Get the timestamp of this transaction
     *
     * @return time in millis
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Get verbose response
     *
     * @return ex. Approved, Declined, Error
     */
    public String getResponse() {
        return response;
    }

    /**
     * Get the response code
     *
     * @return responseCode - A = approved, D = declined, E = error
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Get the amount of this transaction
     *
     * @return
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Get the transaction ID of the transaction
     *
     * @return
     */
    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

}
