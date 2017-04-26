package com.drawingboardapps.appetizecode.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.databinding.ActivityDemoBinding;
import com.drawingboardapps.transactionsdk.BaseTransactionActivity;
import com.drawingboardapps.transactionsdk.Transaction;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionCallback;
import com.drawingboardapps.transactionsdk.TransactionResult;
import com.drawingboardapps.transactionsdk.TransactionService;

/**
 * Activity to demonstrate a simple Transaction SDK, extends {@link com.drawingboardapps.transactionsdk.BaseTransactionActivity}
 * which handles
 * the {@link TransactionService}
 * <p>
 * MVVM pattern used with a Presenter
 * <p>
 * Written by Zach Nwabudike 4/24/2017
 */
public class DemoActivity extends BaseTransactionActivity implements PresenterDelegates {

    private MainPresenterImpl presenter;
    private String TAG = "DemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDemoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        presenter = new MainPresenterImpl(this);
        presenter.initViews(binding);
    }

    @Override
    public void doStartTransaction(TransactionRequest transactionRequest) {
        startTransaction(transactionRequest, new TransactionCallback() {
            @Override
            public void onTransactionComplete(TransactionResult transactionResult) {
                presenter.onTransactionComplete(transactionResult);
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                e.printStackTrace();
                presenter.onError(e);
            }
        });
    }

    @Override
    public FragmentManager getFragManager(){
        return getSupportFragmentManager();
    }

    @Override
    public void doCancelTransaction(TransactionRequest transaction) {
        cancelTransaction(transaction);
    }

    @Override
    public Context getContext() {
        return this;
    }


}
