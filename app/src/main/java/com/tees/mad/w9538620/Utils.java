package com.tees.mad.w9538620;


import android.app.ProgressDialog;
import android.content.Context;

public class Utils {
    private static ProgressDialog dialogProgress;

    public static void showDialog(Context context) {
        dialogProgress = new ProgressDialog(context);
        dialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogProgress.setMessage("Please wait...");
        dialogProgress.setIndeterminate(true);
        dialogProgress.setCanceledOnTouchOutside(false);
        dialogProgress.show();
    }

    public static void dismissDialog() {
        if (dialogProgress != null) {
            dialogProgress.dismiss();
            dialogProgress = null;
        }
    }
}
