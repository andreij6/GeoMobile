package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerDetailParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;

public class GetLayerDetailsTask implements IGetLayerDetailsTask {

    ILayerTreeService mLayerTreeService;

    public GetLayerDetailsTask(){

        mLayerTreeService = application.getTreeServiceComponent().provideLayerTreeService();
    }

    @Override
    public void getDetails(GetLayerDetailParams params) {
        new GetDetailsAsync(params).execute();
    }

    private class GetDetailsAsync extends GeoAsyncTask<Void, Void, LayerDetailsVm> {

        Layer mEntity;
        LayerDetailsVm mDetails;

        public GetDetailsAsync(GetLayerDetailParams params){
            super(params.getExecuter());
            mEntity = params.getLayer();
            mDetails = params.getDetails();
        }

        @Override
        protected LayerDetailsVm doInBackground(Void... params) {

            if(mEntity != null){
                mDetails = mLayerTreeService.getDetails(mEntity.getId());
            }

            return mDetails;
        }

    }
}
