package com.drawingboardapps.transactionsdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * Transaction result POJO
 *
 * //TODO make this a RealmObject
 *
 * Created by Zach on 4/23/2017.
 */

public class TransactionResult implements Serializable {

    @SerializedName("transID")
    @Expose
    String transID = "";
    @SerializedName("timestamp")
    @Expose
    String timestamp = "";
    @SerializedName("amount")
    @Expose
    String amount = "";
    @SerializedName("response")
    @Expose
    String response = "";
    @SerializedName("responseCode")
    @Expose
    String responseCode = "";


    /**
     * Get the timestamp of this transaction
     * @return time in millis
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Get verbose response
     * @return ex. Approved, Declined, Error
     */
    public String getResponse() {
        return response;
    }

    /**
     * Get the response code
     * @return responseCode - A = approved, D = declined, E = error
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Get the amouint of this transaction
     * @return
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Get the transaction ID of the transaction
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
