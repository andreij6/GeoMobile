package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class GetLayerDetailParams extends ExecuterParamsBase{
    Layer mLayer;
    LayerDetailsVm mDetails;

    public GetLayerDetailParams(Layer entity, LayerDetailsVm details, IPostExecuter executer) {
        super(executer);
        mLayer = entity;
        mDetails = details;
    }

    public Layer getLayer() {
        return mLayer;
    }

    public LayerDetailsVm getDetails() {
        return mDetails;
    }
}
