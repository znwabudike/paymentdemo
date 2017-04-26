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
import com.drawingboardapps.appetizecode.databinding.DialogTransResultBinding;
import com.drawingboardapps.appetizecode.viewmodel.VMTransResult;
import com.drawingboardapps.transactionsdk.TransactionResult;

/**
 * Created by Zach on 4/25/2017.
 */

public class TransactionDialogFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "trans_result";
    private TransactionResult transactionResult;

    public TransactionDialogFragment() {}

    public static TransactionDialogFragment newInstance(@NonNull TransactionResult transactionResult) {
        Bundle b = new Bundle();
        TransactionDialogFragment fragment = new TransactionDialogFragment();
        b.putSerializable(ARG_PARAM1, transactionResult);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.transactionResult = (TransactionResult) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DialogTransResultBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()),
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

}
