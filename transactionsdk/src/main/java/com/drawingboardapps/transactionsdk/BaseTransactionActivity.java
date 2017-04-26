package com.drawingboardapps.transactionsdk;

import android.support.v7.app.AppCompatActivity;

/**
 * BaseActivity
 *
 * Created by Zach on 4/23/2017.
 */

public class BaseTransactionActivity extends AppCompatActivity {

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
        TransactionContentProvider.SDK.bindService(this);
    }

    @Override
    protected void onStop() {
        TransactionContentProvider.SDK.unbindService(this);
        super.onStop();
    }

    /**
     * Call on {@link TransactionContentProvider} to start the transaction and delegate error if any.
     * @param request transaction request
     * @param callback callback to
     */
    public void startTransaction(TransactionRequest request, TransactionCallback callback) {
        try {
            TransactionContentProvider.SDK.startTransaction(request, callback);
        } catch (Exception e) { //service not bound
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            callback.onError(e);
        }
    }

}