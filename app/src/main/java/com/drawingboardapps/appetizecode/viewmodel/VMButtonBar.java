package com.drawingboardapps.appetizecode.viewmodel;


import android.databinding.BaseObservable;

import com.drawingboardapps.appetizecode.activity.MainPresenterImpl;

/**
 * Created by Zach on 4/22/2017.
 */

public class VMButtonBar extends BaseObservable{

    private final MainPresenterImpl mainPresenter;

    public VMButtonBar(MainPresenterImpl mainPresenter) {
        this.mainPresenter = mainPresenter;
    }
}
