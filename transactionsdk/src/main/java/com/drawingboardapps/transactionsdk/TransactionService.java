package com.drawingboardapps.transactionsdk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Service using RXAndroid to perform calls to API, return exception is caught and a
 * successful return is faked through the error methods.
 * <p>
 * Created by Zach on 4/23/2017.
 */
public class TransactionService extends Service {
    private final TransactionSDK sdk;
    private BinderImpl binder = new BinderImpl();
    private String TAG = "TransactionService";

    public TransactionService() {
        this.sdk = new TransactionSDK(true);
    }

    public void startTransaction(TransactionRequest request,
                                 final TransactionCallback transactionInterface) throws Exception {
        sdk.startTransaction(request, transactionInterface);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public class BinderImpl extends Binder {
        public TransactionService getService() {
            return TransactionService.this;
        }
    }
}
