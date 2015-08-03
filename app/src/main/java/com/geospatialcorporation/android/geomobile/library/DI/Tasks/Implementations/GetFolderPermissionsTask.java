package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderPermissionTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.FolderPermissionsParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;

import java.util.List;

public class GetFolderPermissionsTask implements IGetFolderPermissionTask {

    IFolderTreeService mFolderTreeService;

    public GetFolderPermissionsTask(){

        mFolderTreeService = application.getTreeServiceComponent().provideFolderTreeService();
    }

    @Override
    public void getPermissions(FolderPermissionsParams params) {
        new GetPermissionsTask(params).execute();
    }

    protected class GetPermissionsTask extends GeoAsyncTask<Void, Void, List<FolderPermissionsResponse>> {

        Folder mEntity;
        List<FolderPermissionsResponse> mPermission;

        public GetPermissionsTask(FolderPermissionsParams params) {
            super(params.getExecuter());
            mEntity = params.getEntity();
            mPermission = params.getPermissions();
        }

        @Override
        protected List<FolderPermissionsResponse> doInBackground(Void... params) {
            if(mEntity != null){
                mPermission = mFolderTreeService.permissions(mEntity.getId());
            }

            return mPermission;
        }
    }
}
