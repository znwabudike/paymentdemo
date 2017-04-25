package com.drawingboardapps.appetizecode.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.drawingboardapps.appetizecode.BuildConfig;
import com.drawingboardapps.appetizecode.service.TransactionService;
import com.drawingboardapps.transactionsdk.TransactionCallback;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionSDK;

/**
 * BaseActivity which gives access to {@link TransactionSDK} and {@link TransactionService}
 *
 * Created by Zach on 4/23/2017.
 */

class BaseTransactionActivity extends AppCompatActivity {

    public TransactionSDK transactionSDK;
    public TransactionService boundService;
    private boolean serviceBound;
    private String TAG = "BaseTransactionActivity";

    public BaseTransactionActivity() {
        transactionSDK = new TransactionSDK();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTransactionService();
    }

    @Override
    protected void onStop() {
        stopTransactionService();
        super.onStop();
    }

    ///////  Transaction Service  /////////
    private void stopTransactionService() {
        unbindService(serviceConnection);
    }

    private void startTransactionService() {
        Intent serviceIntent = new Intent(this, TransactionService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    protected void startTransaction(TransactionRequest request, TransactionCallback callback) {
        try {
            if (!serviceBound) throw new Exception("Service not bound");
            boundService.startTransaction(request, callback);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            callback.onError(e);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            serviceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            TransactionService.BinderImpl myBinder = (TransactionService.BinderImpl) service;
            boundService = myBinder.getService();
            serviceBound = true;
        }
    };
}
