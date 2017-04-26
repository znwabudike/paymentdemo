package com.drawingboardapps.transactionsdk;
import android.content.Context;

/**
 * Warning, call destroy() on this class
 * Created by Zach on 4/23/2017.
 */

public final class TrasnsactionContentProvider {

    private static TransactionSDK sdk;
    static TrasnsactionContentProvider instance;

    private TrasnsactionContentProvider(){}

    /**
     * Initialize the content provider
     * @param context
     * @return
     */
    public static TrasnsactionContentProvider init(Context context) {
        if (instance == null){
            instance = new TrasnsactionContentProvider();
        }
        SDK.initTransactionService(context);
        return instance;
    }

    /**
     * null the singleton instances and let the GC get them
     */
    static void destroy(){
        instance = null;
        sdk = null;
    }

    /**
     * SDK Class to communicate with the {@link TransactionSDK}
     */
    public final static class SDK{

        /**
         * Initialize the {@link TransactionService}
         * @param context
         */
        public static void initTransactionService(Context context) {
            sdk = new TransactionSDK();
            sdk.startService(context);
        }

        /**
         * Start a transaction
         * @param request the request to process
         * @param callback to error or success
         * @throws Exception if the service has not been initialized
         */
        public static void startTransaction(TransactionRequest request, TransactionCallback callback) throws Exception {
            sdk.startTransaction(request, callback);
        }

        /**
         * Call this to bind the service to a particular context
         * @param context we wish to bind to
         * @return true if bound
         */
        public static boolean bindService(Context context){
            return sdk != null && sdk.bindService(context);
        }

        /**
         * Unbind the service because it needs to survive application lifecycle, however, calling
         * this will cause SDK to become null.  The service must be initialized again.
         * @param context
         */
        public static void onDestroy(Context context){
            if (sdk == null) return;
            unbindService(context);
            sdk = null;
        }

        /**
         * Unbind the service from the context
         * @param context
         */
        public static void unbindService(Context context) {
            if (sdk == null) return;
            sdk.unbindService(context);
        }
    }

//    public static TrasnsactionContentProvider init(){
//        if (instance != null){
//            return instance;
//        }else{
//            instance = new TrasnsactionContentProvider();
//            return instance;
//        }
//    }
//
//    static class RealmDriver{
//        public static void saveTransactionOffline(TransactionRequest request){
//            return;
//        }
//
//        public ArrayList<TransactionRequest> retrieveOfflineTransactionRequests(){
//            return null;
//        }
//    }
}
