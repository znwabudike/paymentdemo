package com.drawingboardapps.transactionsdk;

/**
 * Created by Zach on 4/23/2017.
 */

public interface TransactionInterface {

    void onStartTransaction(TransactionRequest transactionRequest);
    void onTransactionComplete(TransactionResult transactionResult);

    void onError(Throwable e);
}
