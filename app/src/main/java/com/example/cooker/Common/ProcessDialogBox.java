package com.example.cooker.Common;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by nachiket on 10/31/2017.
 */

public class ProcessDialogBox {

    private ProgressDialog progressDialog;

    public ProcessDialogBox(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMax(100); // Progress Dialog Max Value
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Processing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
        progressDialog.setCancelable(false);
    }

    public void ShowDialogBox()
    {
        progressDialog.show(); // Display Progress Dialog
    }

    public void DismissDialogBox()
    {
        progressDialog.dismiss();
    }
}
