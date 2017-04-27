package com.drawingboardapps.appetizecode.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 * Created by Zach on 4/25/2017.
 */

public class VMTransResult extends BaseObservable {
    private final TransactionResult result;

    public VMTransResult(TransactionResult result){
        this.result = result;
    }

    /**
     * Get the timestamp of this transaction
     * @return time in millis
     */
    @Bindable
    public String getTimestamp() {
        return result.getTimestamp();
    }

    /**
     * Get verbose response
     * @return ex. Approved, Declined, Error
     */
    @Bindable
    public String getResponse() {
        return result.getResponse();
    }

    /**
     * Get the response code
     * @return responseCode - A = approved, D = declined, E = error
     */
    @Bindable
    public String getResponseCode() {
        return result.getResponseCode();
    }

    /**
     * Get the amount of this transaction
     * @return
     */
    @Bindable
    public String getAmount() {
        return String.format("$%s",result.getAmount());
    }

    /**
     * Get the transaction ID of the transaction
     * @return
     */
    @Bindable
    public String getTransID() {
        return result.getTransID();
    }

    @Bindable
    public String getResponseText(){
        return String.format("Transaction %s", getResponse());
    }
}
