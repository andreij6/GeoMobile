package com.geospatialcorporation.android.geomobile.library.services.LayerDetailsTabCommons;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerDetailParams;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

public class LayerDetailsTabCommon implements ILayerDetailsTabCommon {
    IGetLayerDetailsTask mTask;

    public LayerDetailsTabCommon(){
        mTask = application.getTasksComponent().provideLayerDetailsTask();
    }

    @Override
    public Layer handleArguments(Bundle arguments) {
        return arguments.getParcelable(Layer.LAYER_INTENT);
    }

    @Override
    public void getDetailsAsync(GetLayerDetailParams getLayerDetailParams) {
        mTask.getDetails(getLayerDetailParams);
    }
}
