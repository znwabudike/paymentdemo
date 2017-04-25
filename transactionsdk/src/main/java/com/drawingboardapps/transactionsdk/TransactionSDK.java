package com.drawingboardapps.transactionsdk;

import android.util.Log;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * SDK Class to perform a transaction.
 * <p>
 * Created by Zach on 4/23/2017.
 */
public final class TransactionSDK {
    private final String BASE_URL = "http://www.inchestilzachandjoreunite.com/";
    private String TAG = "TransactionSDK";

    public TransactionSDK() {
    }

    /**
     * Method to start a transaction
     * <B>NOTE: </B>This should not be run on the UI Thread.
     *
     * @param request the request to process
     * @param callback the callback through which results are returned to the Presentation Layer
     */
    public void startTransaction(TransactionRequest request,
                                 final TransactionCallback callback) {

        Log.d(TAG, "startTransaction: ");
        Retrofit retrofit = getClient(BASE_URL);
        TransactionAPI api = retrofit.create(TransactionAPI.class);

        api.startTransaction(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(getSubscriber(callback));
    }

    /**
     * Get the RXJava Observer object through which the TransactionResult is received.
     * @param callback the callback through which results are returned to the Presentation Layer
     * @return
     */
    private Observer<TransactionResult> getSubscriber(final TransactionCallback callback) {
        return new Observer<TransactionResult>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull TransactionResult result) {
                try {
                    Log.d(TAG, "onNext: " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.onTransactionComplete(result);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //normally we would do this but the endpoint is fake
                //so pass a fake transaciton result
//                e.printStackTrace();
//                callback.onError(e);
                callback.onTransactionComplete(getFakeResult());
            }

            @Override
            public void onComplete() {}
        };
    }

    /**
     * Get a fake result because there is no endpoint
     * @return a fake {@link TransactionResult}
     */
    private TransactionResult getFakeResult() {
        TransactionResult result = new TransactionResult();
        result.setTimestamp(System.currentTimeMillis() + "");
        result.setAmount("$1.00");
        result.setTransID("ABC123");
        result.setResponse("Approved");
        result.setResponseCode("A");
        return result;
    }

    /**
     * Create a simple retrofit client, interceptor and security intentionally
     * left out to reduce complexity since the endpoint is not live
     *
     * @param baseUrl URL to perform REST request
     * @return a Retrofit instance
     */
    private Retrofit getClient(String baseUrl) {
        Log.d(TAG, "getClient: ");
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
