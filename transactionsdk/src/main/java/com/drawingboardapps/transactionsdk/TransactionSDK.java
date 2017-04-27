package com.drawingboardapps.transactionsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * SDK Class to perform a transaction.
 * <p>
 * Created by Zach on 4/23/2017.
 */
public final class TransactionSDK {

    private final String TAG = "TransactionSDK";
    private final ServiceHelper serviceHelper;
    private final boolean autosave;
    private boolean serviceBound;
    private TransactionHelper transactionHelper;

    public TransactionSDK(boolean autosave) {
        this.autosave = autosave;
        serviceHelper = new ServiceHelper();
        transactionHelper = new TransactionHelper();
    }

    /**
     * Method to start a transaction
     * <B>NOTE: </B>This should not be run on the UI Thread.
     *
     * @param request  the request to process
     * @param callback the callback through which results are returned to the Presentation Layer
     * @throws Exception if the service is not bound
     */
    public void startTransaction(TransactionRequest request,
                                 final TransactionCallback callback) throws Exception {
        serviceHelper.startTransaction(request, callback);
    }

    public void cancelTransaction(TransactionRequest transaction) {
        serviceHelper.cancelTransaction(transaction);
    }

    /**
     * Start the service, call stopService() to stop
     *
     * @param context
     */
    public void startService(Context context) {
        serviceHelper.startService(context);
    }

    public void stopService(Context context) {
        serviceHelper.stopService(context);
    }

    public void unbindService(Context context) {
        serviceHelper.unbindService();
    }

    public boolean bindService(Context context) {
        return serviceHelper.bindService(context);
    }

    boolean isServiceBount() {
        return serviceHelper.isServiceBound();
    }


    /**
     * Helper class to facilitate anything related to the service
     */
    final private class ServiceHelper {
        private TransactionService boundService;


        final private ServiceConnection serviceConnection = new ServiceConnection() {

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

        private void startTransaction(TransactionRequest transaction, TransactionCallback callback) throws Exception {
            if (! serviceBound) throw new Exception("Service not bound");
            boundService.startTransaction(transaction, callback, autosave);
        }

        private void startService(Context context) {
            context.startService(new Intent(context, TransactionService.class));
        }

        private void stopService(Context context) {
            context.stopService(new Intent(context, TransactionService.class));
        }

        private boolean bindService(Context context) {
            return context.bindService(new Intent(context, TransactionService.class),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE);
        }

        private void unbindService() {
            if (serviceBound)
            boundService.unbindService(serviceConnection);
        }

        private boolean isServiceBound() {
            return serviceBound;
        }

        public void cancelTransaction(TransactionRequest transaction) {
            boundService.cancelTransaction(transaction);
        }
    }

}
