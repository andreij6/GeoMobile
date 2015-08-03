package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;

public class GetFolderDetailsParams extends ExecuterParamsBase {

    Folder mEntity;
    FolderDetailsResponse mDetails;

    public Folder getEntity() {
        return mEntity;
    }

    public FolderDetailsResponse getDetails() {
        return mDetails;
    }



    public GetFolderDetailsParams(Folder entity, FolderDetailsResponse details, IPostExecuter executer) {
        super(executer);
        mEntity = entity;
        mDetails = details;
    }
}
