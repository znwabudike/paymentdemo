package com.drawingboardapps.transactionsdk;

import android.app.Application;

import com.drawingboardapps.transactionsdk.TransactionContentProvider;
import com.drawingboardapps.transactionsdk.TransactionSDK;
import com.squareup.leakcanary.LeakCanary;


/**
 * Application base  which gives access to {@link TransactionSDK} and {@link com.drawingboardapps.transactionsdk.TransactionService}
 * Created by Zach on 4/23/2017.
 */

public class TransactionApplication extends Application {


    private final boolean autoSaveTransactions;

    public TransactionApplication(boolean autoSaveTransactions){
        this.autoSaveTransactions = autoSaveTransactions;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TransactionContentProvider.init(this, autoSaveTransactions);
    }
}
