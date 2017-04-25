package com.drawingboardapps.appetizecode.views;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.activity.KeyboardInterface;
import com.drawingboardapps.appetizecode.databinding.KeyboardBinding;
import com.drawingboardapps.appetizecode.viewmodel.VMKeyboard;

/**
 * Created by Zach on 4/22/2017.
 */

public class KeyboardView extends FrameLayout
//        implements View.OnClickListener
{

    private KeyboardInterface keyboardOutput;
    private KeyboardBinding binding;

    public KeyboardView(Context context) {
        super(context);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = KeyboardBinding.inflate(inflater);
        initViews();
    }

    public void setViewModel(VMKeyboard model){
        binding.setKeyboardModel(model);
    }
    private void initViews() {
//        $(R.id.t9_key_0).setOnClickListener(this);
//        $(R.id.t9_key_1).setOnClickListener(this);
//        $(R.id.t9_key_2).setOnClickListener(this);
//        $(R.id.t9_key_3).setOnClickListener(this);
//        $(R.id.t9_key_4).setOnClickListener(this);
//        $(R.id.t9_key_5).setOnClickListener(this);
//        $(R.id.t9_key_6).setOnClickListener(this);
//        $(R.id.t9_key_7).setOnClickListener(this);
//        $(R.id.t9_key_8).setOnClickListener(this);
//        $(R.id.t9_key_9).setOnClickListener(this);
//        $(R.id.t9_key_clear).setOnClickListener(this);
//        $(R.id.t9_key_backspace).setOnClickListener(this);
    }



    public void setListener(KeyboardInterface keyboardOutput){
        this.keyboardOutput = keyboardOutput;
    }
    public String getInputText() {
        return keyboardOutput.getText().toString();
    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}
