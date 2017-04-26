package com.drawingboardapps.appetizecode.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.drawingboardapps.appetizecode.R;
import com.drawingboardapps.appetizecode.databinding.DialogTransRequestBinding;
import com.drawingboardapps.appetizecode.databinding.DialogTransResultBinding;
import com.drawingboardapps.appetizecode.viewmodel.VMTransRequest;
import com.drawingboardapps.appetizecode.viewmodel.VMTransResult;
import com.drawingboardapps.transactionsdk.Transaction;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 * Created by Zach on 4/25/2017.
 */

public class TransactionDialogFragment extends BaseDialogFragment {

    private static final String ARG_PARAM1 = "trans_result";
    private static final String ARG_PARAM2 = "type";
    private static final int REQUEST = 0;
    private static final int RESULT = 1;

    private TransactionRequest transactionRequest;
    private TransactionResult transactionResult;
    private int type;
    private Object resultDialog;
    private DialogCallback callback;
    public interface DialogCallback{
        void cancelTransaction();
    }

    public TransactionDialogFragment() {}

    public static TransactionDialogFragment newInstance(@NonNull TransactionResult transactionResult,
                                                        @Nullable DialogCallback callback) {
        Bundle b = new Bundle();
        TransactionDialogFragment fragment = new TransactionDialogFragment();
        fragment.setCallback(callback);
        b.putSerializable(ARG_PARAM1, transactionResult);
        b.putInt(ARG_PARAM2, RESULT);
        fragment.setArguments(b);
        return fragment;
    }

    public static TransactionDialogFragment newInstance(TransactionRequest transactionRequest,
                                                        @Nullable DialogCallback callback) {
        Bundle b = new Bundle();
        TransactionDialogFragment fragment = new TransactionDialogFragment();
        fragment.setCallback(callback);
        b.putSerializable(ARG_PARAM1, transactionRequest);
        b.putInt(ARG_PARAM2, REQUEST);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type = getArguments().getInt(ARG_PARAM2);

        switch(type){
            case RESULT:
                this.transactionResult = (TransactionResult) getArguments().getSerializable(ARG_PARAM1);
                break;
            case REQUEST:
                this.transactionRequest = (TransactionRequest) getArguments().getSerializable(ARG_PARAM1);
                break;
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        switch (type) {
            case REQUEST:
                return getRequestDialog();
            case RESULT:
                return getResultDialog();
            default:
                return getErrorDialog(getContext());
        }
    }

    public Dialog getRequestDialog() {
        DialogTransRequestBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()),
                        R.layout.dialog_trans_request,
                        null,
                        false);

        binding.setRequestModel(new VMTransRequest(transactionRequest));

        return new AlertDialog.Builder(getActivity())
                .setTitle(transactionRequest.getTotal())
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (callback != null) {
                                    callback.cancelTransaction();
                                }
                                dismiss();
                            }
                        }
                )
                .setCancelable(false)
                .setView(binding.getRoot())
                .create();
    }

    public Dialog getResultDialog() {
        DialogTransResultBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()),
                        R.layout.dialog_trans_result,
                        null,
                        false);

        binding.setResultModel(new VMTransResult(transactionResult));

        return new AlertDialog.Builder(getActivity())
                .setTitle(transactionResult.getResponse())
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .setView(binding.getRoot())
                .create();
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }


}
