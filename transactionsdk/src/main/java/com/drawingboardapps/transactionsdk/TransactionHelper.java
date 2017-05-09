package com.drawingboardapps.transactionsdk;

/**
 * Created by Zach on 4/26/2017.
 */

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class to facilitate everything for the transaction API calls
 */
public final class TransactionHelper {
    private final String TAG = "TransactionHelper";
    private final String BASE_URL = "http://inchestilzachandjoreunite.com/";

    private ConcurrentHashMap<TransactionRequest, Disposable> disposables = new ConcurrentHashMap<>();
    private ConcurrentHashMap<TransactionRequest, TransactionObserver> observers = new ConcurrentHashMap<>();

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

    protected void startTransaction(final TransactionRequest request,
                                    final TransactionCallback callback,
                                    final boolean autosave) {

        Log.d(TAG, "startTransaction: ");
        Retrofit retrofit = getClient(BASE_URL);
        TransactionAPI api = retrofit.create(TransactionAPI.class);
        final Observable<TransactionResult> observable = api.startTransaction(request);

        TransactionObserver transactionObserver = getSubscriber(request, callback, autosave);
        observers.put(request,transactionObserver);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread()).subscribeWith(transactionObserver);

    }

    public void cancelTransaction(TransactionRequest transaction) {
        disposables.get(transaction).dispose();
        Log.d(TAG, "cancelTransaction: called on " + disposables.get(transaction));
        disposables.remove(transaction);
        observers.remove(transaction).onCancelled();
    }

    /**
     * Get the RXJava Observer object through which the TransactionResult is received.
     *
     * @param request
     * @param callback the callback through which results are returned to the Presentation Layer
     * @return
     */
    private TransactionObserver getSubscriber(final TransactionRequest request,
                                                      final TransactionCallback callback,
                                                      final boolean autosave) {
        return new TransactionObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.put(request, d);
            }

            @Override
            public void onNext(@NonNull TransactionResult result) {
                Log.d(TAG, "onNext: " + result);
                callback.onTransactionComplete(result);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //normally we would do this but the endpoint is fake
                //so pass a fake transaciton result
                //callback.onError(e);

                Log.d(TAG, "onError: ON ERROR CALLED");
                TransactionResult result = getFakeResult(request, false);
                if (autosave) {
                    try {
                        TransactionContentProvider.DATABASE.saveTransactionToHistoryDatabase(result);
                    } catch (Exception e1) {
                        callback.onError(e);
                    }
                }
                callback.onTransactionComplete(result);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called");
                disposables.remove(request);
            }

            @Override
            public void onCancelled() {
                Log.d(TAG, "onCancelled: called");
                TransactionResult result = new TransactionResult();
                result.setResponse(TransactionResult.USER_CANCELLED);
                callback.onTransactionComplete(result);
            }
        };
    }

    /**
     * Get a fake result because there is no endpoint
     *
     * @return a fake {@link TransactionResult}
     */
    private TransactionResult getFakeResult(TransactionRequest request, boolean isCancelled) {
        TransactionResult result = new TransactionResult();
        result.setTimestamp(System.currentTimeMillis() + "");
        Log.d(TAG, "getFakeResult: total =  " + request.getTotal());
        result.setAmount(request.getTotal());
        result.setTransID("ABC123");
        long response = Math.round(Math.random());
        result.setResponse(response == 0 ? "Approved" : "Declined");
        result.setResponseCode(response == 0 ? "A" : "D");
        return result;
    }



    public abstract class TransactionObserver implements Observer<TransactionResult> {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull TransactionResult transactionResult) {

        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }

        public abstract void onCancelled();
    }
}

