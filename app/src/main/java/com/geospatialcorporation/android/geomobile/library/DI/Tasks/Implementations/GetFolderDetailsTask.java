package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetFolderDetailsParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;

public class GetFolderDetailsTask implements IGetFolderDetailsTask {

    IFolderTreeService mFolderTreeService;

    public GetFolderDetailsTask(){
        mFolderTreeService = application.getTreeServiceComponent().provideFolderTreeService();
    }

    @Override
    public void getDetails(GetFolderDetailsParams params) {
        new GetFolderDetailsAsync(params).execute();
    }

    protected class GetFolderDetailsAsync extends GeoAsyncTask<Void, Void, FolderDetailsResponse> {

        Folder mEntity;
        FolderDetailsResponse mDetails;

        public GetFolderDetailsAsync(GetFolderDetailsParams params) {
            super(params.getExecuter());
            mEntity = params.getEntity();
            mDetails = params.getDetails();
        }

        @Override
        protected FolderDetailsResponse doInBackground(Void... params) {

            if(mEntity != null){
                mDetails = mFolderTreeService.details(mEntity.getId());
            }

            return mDetails;
        }
    }
}
