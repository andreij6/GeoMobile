package com.geospatialcorporation.android.geomobile.library.services.LayerDetailsTabCommons;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerDetailParams;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

public interface ILayerDetailsTabCommon {
    Layer handleArguments(Bundle arguments);

    void getDetailsAsync(GetLayerDetailParams getLayerDetailParams);
}
