package com.drawingboardapps.transactionsdk;

import android.content.Context;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Warning, call destroy() on this class
 * Created by Zach on 4/23/2017.
 */

public final class TransactionContentProvider {

    private static TransactionSDK sdk;
    static TransactionContentProvider instance;

    private TransactionContentProvider() {
    }

    /**
     * Initialize the content provider
     *
     * @param context
     * @return
     */
    public static TransactionContentProvider init(Context context, boolean autosave) {
        if (instance == null) {
            instance = new TransactionContentProvider();
        }
        SDK.initTransactionService(context, autosave);
        DATABASE.initRealm(context);
        return instance;
    }

    /**
     * null the singleton instances and let the GC get them
     */
    static void destroy() {
        instance = null;
        sdk = null;
    }



    /**
     * SDK Class to communicate with the {@link TransactionSDK}
     */
    public final static class SDK {

        /**
         * Initialize the {@link TransactionService}
         *
         * @param context
         */
        public static void initTransactionService(Context context, boolean autosave) {
            sdk = new TransactionSDK(autosave);
            sdk.startService(context);
        }

        /**
         * Start a transaction
         *
         * @param request  the request to process
         * @param callback to error or success
         * @throws Exception if the service has not been initialized
         */
        public static void startTransaction(TransactionRequest request, TransactionCallback callback) throws Exception {
            sdk.startTransaction(request, callback);
        }


        /**
         * Cancel the current transaction
         * @param transaction
         */
        public static void cancelTransaction(TransactionRequest transaction) {
            sdk.cancelTransaction(transaction);
        }


        /**
         * Call this to bind the service to a particular context
         *
         * @param context we wish to bind to
         * @return true if bound
         */
        public static boolean bindService(Context context) {
            return sdk != null && sdk.bindService(context);
        }

        /**
         * Unbind the service because it needs to survive application lifecycle, however, calling
         * this will cause SDK to become null.  The service must be initialized again.
         *
         */
        public static void onDestroy() {
            if (sdk == null) return;
            unbindService();
            sdk = null;
        }

        /**
         * Unbind the service from the context
         *
         */
        public static void unbindService() {
            if (sdk == null) return;
            sdk.unbindService();
        }
    }

    public static class DATABASE {
        private static Realm historyRealm;
        private static Realm pendingRealm;
        private static String TRANSACTION_HISTORY = "history.realm";
        private static String TRANSACTIONS_PENDING = "pending.realm";

        public static void initRealm(Context context) {
            Realm.init(context);

            RealmConfiguration historyConfiguration = new RealmConfiguration.Builder()
                    .name(DATABASE.TRANSACTION_HISTORY)
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .migration(new RealmMigration() {
                                   @Override
                                   public void migrate(DynamicRealm realm,
                                                       long oldVersion,
                                                       long newVersion) {
                                       //nothing has changed yet so no migration needed
                                   }
                               }
                    ).build();

            historyRealm = Realm.getInstance(historyConfiguration);

            RealmConfiguration pendingConfiguration = new RealmConfiguration.Builder()
                    .name(DATABASE.TRANSACTIONS_PENDING)
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .migration(new RealmMigration() {
                                   @Override
                                   public void migrate(DynamicRealm realm,
                                                       long oldVersion,
                                                       long newVersion) {
                                       //nothing has changed yet so no migration needed
                                   }
                               }
                    ).build();

            pendingRealm = Realm.getInstance(pendingConfiguration);
        }

        public static void saveTransactionToHistoryDatabase(TransactionResult result) throws Exception {
            if (historyRealm == null) throw new Exception("Realm not initialized");
            historyRealm.beginTransaction();
            historyRealm.copyToRealm(result);
            historyRealm.commitTransaction();
        }
    }
}
