package com.drawingboardapps.appetizecode.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.activity.KeyboardInterface;
import com.drawingboardapps.appetizecode.views.KeyboardView;

/**
 * Created by Zach on 4/23/2017.
 */

public class VMKeyboard extends BaseObservable{

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

    public View.OnClickListener keyClicked(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (keyboardInterface == null) return;
                // handle number button click
                if (v.getTag() != null && "number_button".equals(v.getTag())) {
                    keyboardInterface.append(((TextView) v).getText());
                    return;
                }
                switch (v.getId()) {
                    case R.id.t9_key_clear: { // handle clear button
                        keyboardInterface.setText(null);
                    }
                    break;
                    case R.id.t9_key_backspace: { // handle backspace button
                        // delete one character
                        Editable editable = new SpannableStringBuilder(keyboardInterface.getText());
                        int charCount = editable.length();
                        if (charCount > 0) {
                            editable.delete(charCount - 1, charCount);
                            keyboardInterface.setText(editable.toString());
                        }
                    }
                    break;
                }
            }
        };
    }

}
