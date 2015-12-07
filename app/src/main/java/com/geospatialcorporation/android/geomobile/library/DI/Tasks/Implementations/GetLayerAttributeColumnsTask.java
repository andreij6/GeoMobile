package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerAttributeColumnsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerAttributesTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;

import java.util.List;

public class GetLayerAttributeColumnsTask implements IGetLayerAttributeColumnsTask {

    ILayerTreeService mLayerTreeService;

    public GetLayerAttributeColumnsTask(){

        mLayerTreeService = application.getTreeServiceComponent().provideLayerTreeService();
    }

    @Override
    public void getColumns(GetLayerAttributesTaskParams params) {
        new GetAttributeColumnsAsync(params).execute();
    }

    protected class GetAttributeColumnsAsync extends GeoAsyncTask<Void, Void, List<LayerAttributeColumn>> {
        Layer mEntity;
        Context mContext;
        List<LayerAttributeColumn> mColumns;

        public GetAttributeColumnsAsync(GetLayerAttributesTaskParams params){
            super(params.getExecuter());
            mEntity = params.getEntity();
            mColumns = params.getColumns();
            mContext = params.getContext();

        }

        @Override
        protected List<LayerAttributeColumn> doInBackground(Void... params) {
            if(mEntity != null){
                mColumns = mLayerTreeService.getColumns(mEntity.getId());
            }

            return mColumns;
        }

    }
}
