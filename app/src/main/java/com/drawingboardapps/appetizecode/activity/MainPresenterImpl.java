package com.drawingboardapps.appetizecode.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.drawingboardapps.appetizecode.databinding.ActivityDemoBinding;
import com.drawingboardapps.appetizecode.fragments.TransactionDialogFragment;
import com.drawingboardapps.appetizecode.viewmodel.VMButtonBar;
import com.drawingboardapps.appetizecode.viewmodel.VMKeyboard;
import com.drawingboardapps.appetizecode.viewmodel.VMPriceInput;
import com.drawingboardapps.transactionsdk.Transaction;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 *
 * MainPresenterImpl - Basically acts as a buffer to the Activity or Fragment that hosts it.
 *
 * Created by Zach on 4/23/2017.
 */
public class MainPresenterImpl implements MainPresenter {

    private final PresenterDelegates delegate;


    public MainPresenterImpl(@NonNull PresenterDelegates delegate){
        this.delegate = delegate;
    }

    /**
     * Given a {@link ActivityDemoBinding} bind a {@link VMKeyboard}, {@link VMPriceInput},
     * and {@link VMButtonBar}
     *
     * @param binding parent binding to which the elements should be bound
     */
    public void initViews(@NonNull ActivityDemoBinding binding){
        VMButtonBar buttonBar = initButtonBar(binding);
        binding.setButtonBarModel(buttonBar);

        VMPriceInput priceBar = initPriceBar(binding);
        binding.setPriceModel(priceBar);

        VMKeyboard keyboard = initKeyboard(priceBar, binding);
        binding.setKeyboardModel(keyboard);
    }

    /**
     * Set price bar binding
     * @param binding
     * @return the ViewModel for price input
     */
    private VMPriceInput initPriceBar(@NonNull ActivityDemoBinding binding) {
        return new VMPriceInput(this);
    }

    /**
     * Set keyboard binding
     * @param priceVM
     * @param binding binding for the activity in which it is hosted.
     */
    private VMKeyboard initKeyboard(@NonNull KeyboardInterface priceVM, @NonNull ActivityDemoBinding binding) {
        return new VMKeyboard(priceVM);
    }

    /**
     * Set button bar binding
     * @param binding binding for the activity in which it is hosted.
     */
    private VMButtonBar initButtonBar(@NonNull ActivityDemoBinding binding) {
       return new VMButtonBar(this);
    }

    /**
     * Show the transaction results when they are received.
     * @param transactionResult model to display.
     */
    private void displayTransactionResults(@NonNull TransactionResult transactionResult) {
        showTransactionCompleteDialog(transactionResult);
    }

    /**
     * Show a dialog informing the user that the transaction is complete
     * @param transactionResult model to display.
     */
    public void showTransactionCompleteDialog(@NonNull TransactionResult transactionResult) {
        getTransactionDialog(transactionResult).show(delegate.getFragManager(), "resultDialog");
    }

    DialogFragment getTransactionDialog(TransactionResult transactionResult){
        return TransactionDialogFragment.newInstance(transactionResult);
    }

    /**
     * Show a dialog that the user has encountered an error.
     * @param e
     */
    private void displayError(@NonNull Throwable e) {
        //TODO display an error dialog
    }

    private Transaction transaction = new Transaction();

    /**
     * Reset the current transaction
     */
    public void doCancelTransaction(){
        transaction = new Transaction();
    }


    @Override
    public void onError(@NonNull Throwable e) {
        displayError(e);
    }

    @Override
    public void onTransactionComplete(@NonNull TransactionResult transactionResult) {
        displayTransactionResults(transactionResult);
    }

    @Override
    public void submitClicked(@NonNull String text) {
        if (!text.replace("$","").equals(transaction.getSubtotal().toString())) {
            transaction.setSubtotal(text);
        }

        TransactionRequest request = transaction.buildRequest();
        delegate.doStartTransaction(request);
    }

}
