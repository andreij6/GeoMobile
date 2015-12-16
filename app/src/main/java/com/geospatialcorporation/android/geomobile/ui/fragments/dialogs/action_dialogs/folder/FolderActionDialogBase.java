package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder;

import android.content.Context;
import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;


public class FolderActionDialogBase extends GeoDialogFragmentBase {

    protected Folder mFolder;
    protected IFolderTreeService mService;

    protected final static String FOLDER_DIALOG = "folder_dialog";

    public void init(Folder folder){
        setContext(getActivity());
        mFolder = folder;
        mService = application.getTreeServiceComponent().provideFolderTreeService();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(FOLDER_DIALOG, mFolder);
    }
}
