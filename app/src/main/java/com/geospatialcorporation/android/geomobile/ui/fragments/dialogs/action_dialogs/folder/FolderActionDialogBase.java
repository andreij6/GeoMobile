package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.library.services.FolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/12/2015.
 */
public class FolderActionDialogBase extends GeoDialogFragmentBase {

    Folder mFolder;
    FolderTreeService mService;

    public void init(Context context, Folder folder){
        setContext(context);
        mFolder = folder;
        mService = new FolderTreeService();
    }
}
