package com.drawingboardapps.appetizecode.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;

import com.drawingboardapps.appetizecode.activity.KeyboardInterface;
import com.drawingboardapps.appetizecode.views.KeyboardView;

/**
 * Created by Zach on 4/23/2017.
 */

public class VMKeyboard extends BaseObservable implements KeyboardInterface{

    private final KeyboardInterface keyboardInterface;

    public VMKeyboard(KeyboardInterface mainPresenter) {
        this.keyboardInterface = mainPresenter;
    }

    @Bindable
    public KeyboardInterface getKeyboardInterface(){
        return keyboardInterface;
    }

    @BindingAdapter("initKeyboard")
    public static void initKeyboard(KeyboardView view, KeyboardInterface presenter){
        view.setListener(presenter);
    }


    @Override
    public void append(CharSequence text) {

    }

    @Override
    public void setText(String text) {

    }

    @Override
    public String getText() {
        return null;
    }
}
