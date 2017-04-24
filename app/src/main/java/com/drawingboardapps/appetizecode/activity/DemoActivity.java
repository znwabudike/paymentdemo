package com.drawingboardapps.appetizecode.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.transactionsdk.TransactionAPI;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionEngine;
import com.drawingboardapps.transactionsdk.TransactionInterface;
import com.drawingboardapps.transactionsdk.TransactionResult;
import com.drawingboardapps.transactionsdk.TransactionService;

public class DemoActivity extends DemoBaseActivity implements TransactionInterface {

    private TransactionEngine engine;
    private MainPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        init();
    }

    private void init() {
        presenter = new MainPresenterImpl(this);
        presenter.initViews();
        transactionSDK = new TransactionSDK(this);
    }


    @Override
    public void onStartTransaction(TransactionRequest transactionRequest) {
        engine.startTransaction(transactionRequest, this);
    }

    @Override
    public void onTransactionComplete(TransactionResult transactionResult) {
        presenter.onTransactionComplete(transactionResult);
    }

    @Override
    public void onError(Throwable e) {
        presenter.onError(e);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TransactionAPI.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TransactionService.BinderImpl myBinder = (TransactionService.BinderImpl) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }
    };
}
