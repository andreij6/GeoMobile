package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by andre on 6/24/2015.
 */
public abstract class FeatureMapperBase<T> implements IFeatureMapper {
    GoogleMap mMap;
    T mMapFeature;

    public FeatureMapperBase(){
        mMap = application.getGoogleMap();
    }

    public abstract IFeatureMapper draw(Feature feature);

    public abstract IFeatureMapper addStyle(Style style);

    public abstract void commit();

    public abstract void reset();
}
