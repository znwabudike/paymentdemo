package com.drawingboardapps.appetizecode.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.drawingboardapps.appetizecode.BR;
import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.activity.KeyboardInterface;
import com.drawingboardapps.appetizecode.activity.MainPresenter;

/**
 * Created by Zach on 4/22/2017.
 */

public class VMPriceInput extends BaseObservable implements KeyboardInterface {

    private final MainPresenter presenter;
    ObservableField<String> text = new ObservableField<>("$0.00");

    public VMPriceInput(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void append(CharSequence c) {
        text.set(fixText(String.format("%s%s", text.get(), c)));
        notifyPropertyChanged(BR.text);
    }

    @Override
    public void setText(String txt) {
        text.set(fixText(txt));
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getText() {
        return text.get();
    }

    public String fixText(String text){
        if (text == null) return "";

//        if (!text.matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
            String userInput = "" + text.replaceAll("[^\\d]", "").replace("$","");
            StringBuilder cashAmountBuilder = new StringBuilder(userInput);

            while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                cashAmountBuilder.deleteCharAt(0);
            }
            while (cashAmountBuilder.length() < 3) {
                cashAmountBuilder.insert(0, '0');
            }
            cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

            return "$" + cashAmountBuilder.toString();

//            txt.setText(cashAmountBuilder.toString());
//            view.setTextKeepState("$" + cashAmountBuilder.toString());
//            Selection.setSelection(view.getText(), cashAmountBuilder.toString().length() + 1);

//        }
//        return "";
    }

    @BindingAdapter("textWatcher")
    public static void textWatcher(final EditText view, final String text){

    }


    public View.OnClickListener submit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text.get().equals(v.getContext().getString(R.string.hint_init_zero))) {
                    presenter.onError(new Exception(v.getContext().getString(R.string.txt_invalid_amt)));
                    return;
                }

                presenter.submitClicked(text.get());
            }
        };
    }


}
