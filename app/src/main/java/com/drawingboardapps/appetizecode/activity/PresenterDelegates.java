package com.drawingboardapps.appetizecode.activity;

import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 * Created by zach on 4/24/17.
 */

interface PresenterDelegates {
    void doStartTransaction(TransactionRequest request);
}
