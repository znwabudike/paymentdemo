package com.drawingboardapps.appetizecode.application;

import android.app.Application;

import com.drawingboardapps.transactionsdk.DemoContentProvider;
import com.drawingboardapps.transactionsdk.TransactionSDK;
import com.squareup.leakcanary.LeakCanary;


/**
 * Application base  which gives access to {@link TransactionSDK} and {@link com.drawingboardapps.transactionsdk.TransactionService}
 * Created by Zach on 4/23/2017.
 */

public class DemoApplication extends Application {

    final public boolean leakCanaryEnabled = true;

    @Override
    public void onCreate() {
        super.onCreate();
        DemoContentProvider.init(this);

        if (leakCanaryEnabled) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }
}
