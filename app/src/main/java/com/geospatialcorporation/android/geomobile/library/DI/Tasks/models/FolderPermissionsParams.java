package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;

import java.util.List;

public class FolderPermissionsParams extends ExecuterParamsBase {

    Folder mEntity;
    List<FolderPermissionsResponse> mPermissions;

    public FolderPermissionsParams(Folder entity, List<FolderPermissionsResponse> permission, IPostExecuter executer) {
        super(executer);
        mEntity = entity;
        mPermissions = permission;
    }

    public Folder getEntity() {
        return mEntity;
    }

    public List<FolderPermissionsResponse> getPermissions() {
        return mPermissions;
    }

}
