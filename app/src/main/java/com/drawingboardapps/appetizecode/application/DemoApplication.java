package com.drawingboardapps.appetizecode.application;

import android.app.Application;


/**
 * Created by Zach on 4/23/2017.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DemoContentProvider.init();
    }

}
