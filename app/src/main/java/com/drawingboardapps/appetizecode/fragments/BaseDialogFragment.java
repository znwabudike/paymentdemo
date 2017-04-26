package com.drawingboardapps.appetizecode.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by zach on 4/26/17.
 */

public class BaseDialogFragment extends DialogFragment {

    public Dialog getErrorDialog(Context context){
        return new AlertDialog.Builder(context)
                .setTitle("Error")
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .setCancelable(true)
                .create();
    }

    public Dialog getErrorDialog(String message, Context context){
        return new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(message)
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .setCancelable(true)
                .create();
    }
}
