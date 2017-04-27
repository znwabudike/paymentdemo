package com.drawingboardapps.appetizecode.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.databinding.ActivityDemoBinding;
import com.drawingboardapps.appetizecode.fragments.BaseDialogFragment;
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
    private TransactionRequest request;
    private TransactionDialogFragment waitFragment;


    public MainPresenterImpl(@NonNull PresenterDelegates delegate){
        this.delegate = delegate;
    }

    VMButtonBar buttonBar;
    VMPriceInput priceBar;
    VMKeyboard keyboard;
    /**
     * Given a {@link ActivityDemoBinding} bind a {@link VMKeyboard}, {@link VMPriceInput},
     * and {@link VMButtonBar}
     *
     * @param binding parent binding to which the elements should be bound
     */
    void initViews(@NonNull ActivityDemoBinding binding){
//        buttonBar = initButtonBar(binding);
//        binding.setButtonBarModel(buttonBar);

        priceBar = initPriceBar(binding);
        binding.setPriceModel(priceBar);

        keyboard = initKeyboard(priceBar, binding);
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
    private void showTransactionCompleteDialog(@NonNull TransactionResult transactionResult) {
        getTransactionDialog(transactionResult).show(delegate.getFragManager(), "resultDialog");
    }

    private DialogFragment getTransactionDialog(TransactionResult transactionResult){
        //TODO implement retry callback
        return TransactionDialogFragment.newInstance(transactionResult, null);
    }

    /**
     * Show a dialog that the user has encountered an error.
     * @param e
     */
    private void displayError(@NonNull Throwable e) {
        Toast.makeText(delegate.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        //TODO pass callback with options if any
        new BaseDialogFragment().getErrorDialog(e.getMessage(), delegate.getContext()).show();
    }

    private Transaction transaction = new Transaction();

//    /**
//     * Reset the current transaction
//     */
//    @Override
//    public void doCancelTransaction(){
//        delegate.doCancelTransaction(request);
//        transaction = new Transaction();
//        request = null;
//    }

    final private String TAG = "MainPresebterImpl";

    @Override
    public void onError(@NonNull Throwable e) {
        Log.d(TAG, "onError: Error3");
        displayError(e);
    }

    @Override
    public void onTransactionComplete(@NonNull TransactionResult transactionResult) {
        if (waitFragment != null) {
            waitFragment.dismiss();
            waitFragment = null;
        }
        resetPriceBar();
        transaction = new Transaction();
        displayTransactionResults(transactionResult);
    }

    private void resetPriceBar() {
        priceBar.setText(delegate.getContext().getString(R.string.hint_init_zero));
    }

    @Override
    public void submitClicked(@NonNull String text) {
        if (!text.replace("$","").equals(transaction.getSubtotal().toString())) {
            try {
                transaction.setSubtotal(text);
            } catch (Exception e) {
                new BaseDialogFragment().getErrorDialog(e.getMessage(), delegate.getContext()).show();
                return;
            }
        }
        TransactionRequest request = transaction.buildRequest();
        //show waiting dialog
        waitFragment = TransactionDialogFragment.newInstance(request,
                getCancelTransactionDelegate(request));
        waitFragment.show(delegate.getFragManager(),"waiting");
        //send the request
        delegate.doStartTransaction(request);
    }


    private TransactionDialogFragment.DialogCallback getCancelTransactionDelegate(final TransactionRequest request) {
        return new TransactionDialogFragment.DialogCallback() {
            @Override
            public void cancelTransaction() {
                cancelTransacitonRequest(request);
            }
        };
    }

    private void cancelTransacitonRequest(TransactionRequest request){
        transaction = new Transaction();
        delegate.doCancelTransaction(request);
    }

    public void onTransactionCancelled() {
        //TODO display cancel success
    }
}
