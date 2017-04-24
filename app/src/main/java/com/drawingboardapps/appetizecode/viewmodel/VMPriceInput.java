package com.drawingboardapps.appetizecode.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.drawingboardapps.appetizecode.BR;
import com.drawingboardapps.appetizecode.activity.KeyboardInterface;
import com.drawingboardapps.appetizecode.activity.MainPresenter;

/**
 * Created by Zach on 4/22/2017.
 */

public class VMPriceInput extends BaseObservable implements KeyboardInterface {

    ObservableField<String> text = new ObservableField<>("$0.00");

    public VMPriceInput(MainPresenter presenter){}

    @Override
    public void append(CharSequence c) {
        text.set(String.format("%s%s", text.get(), c));
    }

    @Override
    public void setText(String txt) {
        text.set(txt);
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    @Override
    public String getText() {
        return text.get();
    }

    public void submit(){

    }
}
