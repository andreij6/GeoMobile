package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.AppSourceBase;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

/**
 * Created by andre on 6/3/2015.
 */
public class LayersAppSource extends AppSourceBase<Layer> {

    public LayersAppSource(){
        super(application.getLayerHashMap());
    }
}
