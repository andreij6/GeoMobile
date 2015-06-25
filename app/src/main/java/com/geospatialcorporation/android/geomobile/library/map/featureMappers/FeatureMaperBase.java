package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by andre on 6/24/2015.
 */
public abstract class FeatureMaperBase implements IFeatureMapper {
    GoogleMap mMap;

    public FeatureMaperBase(){
        mMap = application.getGoogleMap();
    }

    public abstract void draw(Feature feature);
}
