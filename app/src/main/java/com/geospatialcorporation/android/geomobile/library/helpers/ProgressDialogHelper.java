package com.geospatialcorporation.android.geomobile.library.helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by andre on 5/29/2015.
 */
public class ProgressDialogHelper {
    ProgressDialog mProgressDialog;

    //login is the only process that consistently takes time.  the other dialogs just flash by - either change styling, or set a minimum time to show dialog

    public ProgressDialogHelper(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    public void toggleProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        } else {
            mProgressDialog.show();
        }
    }

    public void setProperties(String message, int style){
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(style);
    }

}
