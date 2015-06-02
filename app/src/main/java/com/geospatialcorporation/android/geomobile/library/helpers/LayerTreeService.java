package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;

/**
 * Created by andre on 6/1/2015.
 */
public class LayerTreeService {

    LayerService mLayerService;

    public LayerTreeService(){
        mLayerService = application.getRestAdapter().create(LayerService.class);
    }

    public boolean createLayer(String name, int shapeCode, int parentFolder){
        return false;
    }
}
