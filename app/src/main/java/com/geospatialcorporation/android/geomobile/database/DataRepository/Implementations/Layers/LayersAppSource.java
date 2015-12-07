package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.AppSourceBase;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

public class LayersAppSource extends AppSourceBase<Layer> {

    public LayersAppSource(){
        super(application.getLayerHashMap());
    }
}
