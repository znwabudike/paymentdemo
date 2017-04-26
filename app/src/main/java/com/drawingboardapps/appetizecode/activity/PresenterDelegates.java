package com.drawingboardapps.appetizecode.activity;

import android.support.v4.app.FragmentManager;

import com.drawingboardapps.transactionsdk.TransactionRequest;

/**
 * Created by zach on 4/24/17.
 */

interface PresenterDelegates {
    void doStartTransaction(TransactionRequest request);

    FragmentManager getFragManager();
}
