package com.drawingboardapps.appetizecode.application;

import com.drawingboardapps.transactionsdk.BuildConfig;
import com.drawingboardapps.transactionsdk.TransactionApplication;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Zach on 4/26/2017.
 */

public class DemoApplication extends TransactionApplication {
    final private boolean leakCanaryEnabled = true;

    public DemoApplication() {
        super(true);
        if (leakCanaryEnabled && BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }

}
