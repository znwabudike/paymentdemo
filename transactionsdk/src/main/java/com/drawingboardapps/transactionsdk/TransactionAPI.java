package com.drawingboardapps.transactionsdk;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zach on 4/24/17.
 */

interface TransactionAPI {

    @POST("/testapi/{request}")
    Observable<TransactionResult> startTransaction(@Body TransactionRequest request);

    @POST("/testapi/{request}")
    Callable<TransactionResult> startTransactionCallable(TransactionRequest request);
}
