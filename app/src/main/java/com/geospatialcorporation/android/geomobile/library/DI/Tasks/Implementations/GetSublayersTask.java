package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetSublayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetSublayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ISublayerTreeService;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

public class GetSublayersTask implements IGetSublayersTask {

    ISublayerTreeService mSublayerTreeService;

    public GetSublayersTask(){
        mSublayerTreeService = application.getTreeServiceComponent().provideSublayerTreeService();
    }

    @Override
    public void getSublayers(GetSublayersTaskParams params){
        new GetSublayersAsync(params).execute();
    }

    protected class GetSublayersAsync extends GeoAsyncTask<Void, Void, List<Layer>> {

        List<Layer> mData;
        Context mContext;
        Layer mEntity;


        public GetSublayersAsync(GetSublayersTaskParams params) {
            super(params.getExecuter());
            mEntity = params.getLayer();
            mContext = params.getContext();
            mData = params.getData();
        }

        @Override
        protected List<Layer> doInBackground(Void... params) {
            mData = new ArrayList<>();

            try {
                if (mEntity != null) {
                    mData = mSublayerTreeService.getSublayersByLayerId(mEntity.getId());
                }
            } catch (Exception e){
                //Toaster(e.getMessage());
            }

            return mData;
        }

    }
}
