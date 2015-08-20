package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;

/**
 * Created by andre on 6/26/2015.
 */
public abstract class SingleFeatureMapperBase<T> extends FeatureMapperBase<T> {

    protected ILayerManager mLayerManager;
    protected String mFeatureId;

    public SingleFeatureMapperBase(){
        mLayerManager = application.getMapComponent().provideLayerManager();
    }

    public abstract int addStyles(T option, Style style);

    public abstract void addMapObject(LegendLayer layer, T option);

    public abstract void drawFeature(Geometry feature, T option);

    public void setFeatureId(String featureId) {
        mFeatureId = featureId;
    }
}
