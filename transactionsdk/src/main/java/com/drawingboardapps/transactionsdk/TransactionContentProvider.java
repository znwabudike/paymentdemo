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
    public static TransactionContentProvider init(Context context) {
        if (instance == null) {
            instance = new TransactionContentProvider();
        }
        SDK.initTransactionService(context);
        DATABASE.initRealm();
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
        public static void initTransactionService(Context context) {
            sdk = new TransactionSDK();
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
         * @param context
         */
        public static void onDestroy(Context context) {
            if (sdk == null) return;
            unbindService(context);
            sdk = null;
        }

        /**
         * Unbind the service from the context
         *
         * @param context
         */
        public static void unbindService(Context context) {
            if (sdk == null) return;
            sdk.unbindService(context);
        }
    }

    private static class DATABASE {
        private static Realm historyRealm;
        private static Realm pendingRealm;
        private static String TRANSACTION_HISTORY;
        private static String TRANSACTIONS_PENDING;

        public static void initRealm() {
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
    }
}
