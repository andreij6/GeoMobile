package com.geospatialcorporation.android.geomobile.library.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import com.geospatialcorporation.android.geomobile.R;

/**
 * Created by andre on 5/29/2015.
 */
public class ProgressDialogHelper {
    Context mContext;
    ProgressDialog mProgressDialog;

    public ProgressDialogHelper(Context context)
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.loading_dots));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void toggleProgressDialog() {
        if(mProgressDialog.isShowing()) {
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
