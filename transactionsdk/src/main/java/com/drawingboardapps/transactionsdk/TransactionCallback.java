package com.drawingboardapps.transactionsdk;

import android.support.annotation.NonNull;

/**
 * Created by Zach on 4/23/2017.
 */
public interface TransactionCallback {

    /**
     * Called when the transaction is complete
     * @param transactionResult the result of the transaction
     */
    void onTransactionComplete(@NonNull TransactionResult transactionResult);

    /**
     * Called when there is an error
     * @param e exception details
     */
    void onError(@NonNull Throwable e);
}
