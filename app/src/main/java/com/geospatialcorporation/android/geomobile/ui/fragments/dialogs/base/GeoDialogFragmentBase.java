package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;

public class GeoDialogFragmentBase extends DialogFragment {
    //region Getters & Setters
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    //endregion
    protected Context mContext;

    protected IGeoAnalytics mAnalytics;

    protected AlertDialog.Builder getDialogBuilder(){
        return new AlertDialog.Builder(mContext);
    }

    protected View getDialogView(int layoutId){
        LayoutInflater inflater = LayoutInflater.from(mContext);

        return inflater.inflate(layoutId, null);
    }

    protected void Toaster(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void close() {
        getDialog().cancel();
    }
}
