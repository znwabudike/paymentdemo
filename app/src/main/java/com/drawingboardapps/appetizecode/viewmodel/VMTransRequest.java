package com.drawingboardapps.appetizecode.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.FrameLayout;

import com.drawingboardapps.transactionsdk.LineItem;
import com.drawingboardapps.transactionsdk.TransactionRequest;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by zach on 4/26/17.
 */

public class VMTransRequest extends BaseObservable{

    private final TransactionRequest transactionRequest;

    public VMTransRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    @Bindable
    public String getTotal(){
        return transactionRequest.getTotal();
    }

    @Bindable
    public String getTax(){
        return transactionRequest.getTax();
    }

    @Bindable
    public String getSubtotal(){
        return transactionRequest.getSubtotal();
    }


}
