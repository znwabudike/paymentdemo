package com.drawingboardapps.appetizecode.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.drawingboardapps.transactionsdk.TransactionAPI;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionService;

/**
 * Created by Zach on 4/23/2017.
 */

class TransactionSDK {
    private boolean serviceBound;
    TransactionService service;

    public TransactionSDK() {
    }

    public void startTransaction(TransactionRequest request) throws Exception {
        if (!serviceBound) throw new Exception("Service not bound");
        service.startTransaction(request);
    }
    public void start(Context context) {
        Intent intent = new Intent(context, TransactionAPI.class);
        context.startService(intent);
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stop(Context context) {
        if (serviceBound) {
            context.unbindService(mServiceConnection);
            serviceBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TransactionService.BinderImpl myBinder = (TransactionService.BinderImpl) service;
            service = (IBinder) myBinder.getService();
            serviceBound = true;
        }
    };
}
