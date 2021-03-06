package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

public class CreateDialogFragmentBase extends GeoDialogFragmentBase {
    //region Getters & Setters
    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }
    //endregion



    public void init(Folder folder){
        setContext(getActivity());
        setFolder(folder);
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
    }

    protected Folder mFolder;
    protected IGeoAnalytics mAnalytics;

    protected AlertDialog.Builder getDialogBuilder(){
        return new AlertDialog.Builder(getContext());
    }

    protected View getDialogView(int layoutId){
        LayoutInflater inflater = LayoutInflater.from(getContext());

        return inflater.inflate(layoutId, null);
    }
}
