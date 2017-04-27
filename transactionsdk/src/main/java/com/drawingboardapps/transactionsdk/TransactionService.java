package com.drawingboardapps.transactionsdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service using RXAndroid to perform calls to API, return exception is caught and a
 * successful return is faked through the error methods.
 * <p>
 * Created by Zach on 4/23/2017.
 */
public class TransactionService extends Service {
    private TransactionSDK sdk;
    private TransactionHelper transactionHelper;
    private BinderImpl binder = new BinderImpl();
    private String TAG = "TransactionService";
    private boolean bound;

    public TransactionService() {

    }

    public void startTransaction(TransactionRequest request,
                                 final TransactionCallback transactionInterface, boolean autosave){
        transactionHelper.startTransaction(request, transactionInterface, autosave);
    }

    public void cancelTransaction(TransactionRequest request){
        transactionHelper.cancelTransaction(request);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        this.sdk = new TransactionSDK(true);
        this.transactionHelper = new TransactionHelper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        bound = binder.isBinderAlive();
        return binder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy:  \"service done\"");
        super.onDestroy();
    }

    public boolean isBound() {
        bound = binder.isBinderAlive();
        return bound;
    }

    public class BinderImpl extends Binder {
        public TransactionService getService() {
            return TransactionService.this;
        }
    }


}
