package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

/**
 * Created by andre on 6/1/2015.
 */
public class CreateDialogFragmentBase extends GeoDialogFragmentBase {
    //region Getters & Setters
    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }
    //endregion


    public void init(Context context, Folder folder){
        setContext(context);
        setFolder(folder);
    }

    protected Folder mFolder;

    protected AlertDialog.Builder getDialogBuilder(){
        return new AlertDialog.Builder(mContext);
    }

    protected View getDialogView(int layoutId){
        LayoutInflater inflater = LayoutInflater.from(mContext);

        return inflater.inflate(layoutId, null);
    }
}
