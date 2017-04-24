package com.drawingboardapps.appetizecode.activity;

import com.drawingboardapps.appetizecode.viewmodel.VMButtonBar;
import com.drawingboardapps.appetizecode.viewmodel.VMKeyboard;
import com.drawingboardapps.appetizecode.viewmodel.VMPriceInput;
import com.drawingboardapps.transactionsdk.TransactionInterface;

/**
 * Created by Zach on 4/23/2017.
 */

public class MainPresenterImpl implements MainPresenter {

    private final TransactionInterface transactionInterface;
    private VMPriceInput priceVM;
    private VMKeyboard keyboard;
    private VMButtonBar buttonBar;

    public MainPresenterImpl(TransactionInterface transactionInterface) {
        this.transactionInterface = transactionInterface;
    }

    public void initViews(){
        initButtonBar();
        initKeyboard(initPriceBar());
    }

    private VMPriceInput initPriceBar() {
        priceVM = new VMPriceInput(this);
        return priceVM;
    }

    private void initKeyboard(KeyboardInterface priceVM) {
        keyboard = new VMKeyboard(priceVM);
    }

    private void initButtonBar() {
        buttonBar = new VMButtonBar(this);
    }

    @Override
    public void submitClicked(String text) {
        String total = priceVM.getText();
    }
}
