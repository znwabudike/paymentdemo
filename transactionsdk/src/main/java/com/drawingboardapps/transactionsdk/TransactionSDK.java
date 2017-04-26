package com.drawingboardapps.transactionsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * SDK Class to perform a transaction.
 * <p>
 * Created by Zach on 4/23/2017.
 */
public final class TransactionSDK {
    private final String BASE_URL = "http://www.inchestilzachandjoreunite.com/";
    private final String TAG = "TransactionSDK";
    private final ServiceHelper serviceHelper;
    private final boolean autosave;
    private TransactionHelper transactionHelper;
    private boolean serviceBound;

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
     */
    public void startTransaction(TransactionRequest request,
                                 final TransactionCallback callback) throws Exception {
        if (!serviceBound) {
            throw new Exception("Service not bound");
        }
        transactionHelper.startTransaction(request, callback);
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
        serviceHelper.unbindService(context);
    }

    public boolean bindService(Context context) {
        return serviceHelper.bindService(context);
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

        private void unbindService(Context context) {
            context.unbindService(serviceConnection);
        }

    }

    /**
     * Helper class to facilitate everything for the transaction API calls
     */
    final private class TransactionHelper {
        /**
         * Create a simple retrofit client, interceptor and security intentionally
         * left out to reduce complexity since the endpoint is not live
         *
         * @param baseUrl URL to perform REST request
         * @return a Retrofit instance
         */
        private Retrofit getClient(String baseUrl) {
            Log.d(TAG, "getClient: ");
            return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        private void startTransaction(TransactionRequest request, TransactionCallback callback) {
            Retrofit retrofit = getClient(BASE_URL);
            TransactionAPI api = retrofit.create(TransactionAPI.class);

            api.startTransaction(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(getSubscriber(callback));
        }


        /**
         * Get the RXJava Observer object through which the TransactionResult is received.
         *
         * @param callback the callback through which results are returned to the Presentation Layer
         * @return
         */
        private Observer<TransactionResult> getSubscriber(final TransactionCallback callback) {
            return new Observer<TransactionResult>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull TransactionResult result) {
                    try {
                        Log.d(TAG, "onNext: " + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onTransactionComplete(result);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    //normally we would do this but the endpoint is fake
                    //so pass a fake transaciton result
//                e.printStackTrace();
//                callback.onError(e);
                    TransactionResult result = getFakeResult();
                    if (autosave){
                        TransactionContentProvider.saveTransactionToHistoryDatabase(result);
                    }
                    callback.onTransactionComplete(result);
                }

                @Override
                public void onComplete() {
                }
            };
        }

        /**
         * Get a fake result because there is no endpoint
         *
         * @return a fake {@link TransactionResult}
         */
        private TransactionResult getFakeResult() {
            TransactionResult result = new TransactionResult();
            result.setTimestamp(System.currentTimeMillis() + "");
            result.setAmount("$1.00");
            result.setTransID("ABC123");
            result.setResponse("Approved");
            result.setResponseCode("A");
            return result;
        }
    }
}
