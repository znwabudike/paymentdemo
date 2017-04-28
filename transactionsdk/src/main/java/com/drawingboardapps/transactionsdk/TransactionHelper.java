package com.drawingboardapps.transactionsdk;

/**
 * Created by Zach on 4/26/2017.
 */

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
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

    private ConcurrentHashMap<TransactionRequest, Observable> subscribers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<TransactionRequest, Scheduler> threads = new ConcurrentHashMap<>();

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
        //Test
//        RxJavaPlugins.setErrorHandler(Functions.<Throwable>emptyConsumer());

        Log.d(TAG, "startTransaction: ");
        Retrofit retrofit = getClient(BASE_URL);
        TransactionAPI api = retrofit.create(TransactionAPI.class);
        final Observable<TransactionResult> observable = api.startTransaction(request);

        Scheduler subscribing = Schedulers.newThread();
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
//        observable.fromCallable()
        observable.subscribe(getSubscruber());

        subscribing.shutdown();

        Observable<TransactionResult> ob = Observable.fromCallable(api.startTransactionCallable(request));
        Observer<TransactionResult> ob2 = ob.subscribeWith(new Observer<TransactionResult>() {
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
        });

        // TODO: 4/27/2017 Switch above with below
//        observable.subscribe(getSubscriber(request, callback, autosave));

        threads.put(request, subscribing);
        subscribers.put(request, observable);
    }

    public void cancelTransaction(TransactionRequest transaction) {
        Log.d(TAG, "cancelTransaction: called on " + threads.get(transaction));
        subscribers.get(transaction).unsubscribeOn(threads.get(transaction));
        subscribers.remove(transaction);
        threads.remove(transaction);
    }

public Subscriber<TransactionResult> getSubscruber(){
    return new Subscriber<TransactionResult>() {
        @Override
        public void onSubscribe(Subscription s) {

        }

        @Override
        public void onNext(TransactionResult transactionResult) {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {

        }
    };
}
    private Subscription subMe(){
        return new Subscription() {
            @Override
            public void request(long n) {

            }

            @Override
            public void cancel() {

            }
        };
    }

    private abstract class TransactionConsumer implements Consumer<TransactionResult> {

    }

    private TransactionConsumer getTransactionConsumer(){
        return new TransactionConsumer(){

            @Override
            public void accept(@NonNull TransactionResult transactionResult) throws Exception {

            }
        };
    }
    private Subscriber<TransactionResult> sub(){
        return new Subscriber<TransactionResult>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(TransactionResult transactionResult) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
    /**
     * Get the RXJava Observer object through which the TransactionResult is received.
     *
     * @param request
     * @param callback the callback through which results are returned to the Presentation Layer
     * @return
     */
    private Observer<TransactionResult> getSubscriber(final TransactionRequest request,
                                                       final TransactionCallback callback,
                                                       final boolean autosave) {
        return new Observer<TransactionResult>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull TransactionResult result) {
                Log.d(TAG, "onNext: " + result);
                callback.onTransactionComplete(result);
                subscribers.remove(request);
                threads.remove(request);
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
}

