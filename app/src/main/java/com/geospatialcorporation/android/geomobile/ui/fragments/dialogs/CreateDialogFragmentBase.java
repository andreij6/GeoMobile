package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

/**
 * Created by andre on 6/1/2015.
 */
public class CreateDialogFragmentBase extends DialogFragment {
    //region Getters & Setters
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }
    //endregion

    protected Context mContext;
    protected Folder mFolder;

    protected AlertDialog.Builder getDialogBuilder(){
        return new AlertDialog.Builder(mContext);
    }

    protected View getDialogView(int layoutId){
        LayoutInflater inflater = LayoutInflater.from(mContext);

        return inflater.inflate(layoutId, null);
    }
}
