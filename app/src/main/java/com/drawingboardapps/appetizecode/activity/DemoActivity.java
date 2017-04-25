package com.drawingboardapps.appetizecode.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.databinding.ActivityDemoBinding;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionCallback;
import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 * Activity to demonstrate a simple Transaction SDK, extends {@link BaseTransactionActivity}
 * which handles
 * the {@link com.drawingboardapps.appetizecode.service.TransactionService}
 *
 * MVVM pattern used with a Presenter
 *
 * Written by Zach Nwabudike 4/24/2017
 */
public class DemoActivity extends BaseTransactionActivity implements PresenterDelegates{

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
        try {
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
        } catch (Exception e) {
            presenter.onError(e);
        }
    }

}
