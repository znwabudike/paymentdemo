package com.drawingboardapps.appetizecode.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.drawingboardapps.appetizecode.BuildConfig;
import com.drawingboardapps.appetizecode.application.DemoApplication;
import com.drawingboardapps.appetizecode.application.DemoContentProvider;
import com.drawingboardapps.appetizecode.service.TransactionService;
import com.drawingboardapps.transactionsdk.TransactionCallback;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionSDK;

/**
 * BaseActivity
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
        DemoContentProvider.SDK.bindService(this);
    }

    @Override
    protected void onStop() {
        DemoContentProvider.SDK.unbindService(this);
        super.onStop();
    }

    /**
     * Call on {@link DemoContentProvider} to start the transaction and delegate error if any.
     * @param request transaction request
     * @param callback callback to
     */
    protected void startTransaction(TransactionRequest request, TransactionCallback callback) {
        try {
            DemoContentProvider.SDK.startTransaction(request, callback);
        } catch (Exception e) { //service not bound
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            callback.onError(e);
        }
    }

}
