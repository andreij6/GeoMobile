package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;


public class FolderActionDialogBase extends GeoDialogFragmentBase {

    Folder mFolder;
    IFolderTreeService mService;

    public void init(Context context, Folder folder){
        setContext(context);
        mFolder = folder;
        mService = application.getTreeServiceComponent().provideFolderTreeService();
    }
}
