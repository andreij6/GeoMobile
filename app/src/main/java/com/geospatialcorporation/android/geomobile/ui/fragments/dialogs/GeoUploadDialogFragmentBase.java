package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.DocumentService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

/**
 * Created by andre on 6/2/2015.
 */
public class GeoUploadDialogFragmentBase extends GeoDialogFragmentBase {

    //region Getters & Setters
    public DocumentService getService() {
        return mService;
    }

    public void setService(DocumentService service) {
        mService = service;
    }

    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }
    //endregion

    DocumentService mService;
    Folder mFolder;

    public void init(Context context, Folder folder){
        setContext(context);
        setFolder(folder);
        mService = application.getRestAdapter().create(DocumentService.class);
    }
}
