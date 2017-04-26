package com.drawingboardapps.appetizecode.activity;

import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 * Created by Zach on 4/23/2017.
 */

public interface MainPresenter{

    void submitClicked(String text);

    void onError(Throwable e);

//    void doCancelTransaction();

    void onTransactionComplete(TransactionResult transactionResult);
}
