package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by andre on 6/24/2015.
 */
public abstract class FeatureMapperBase<T> implements IFeatureMapper {
    GoogleMap mMap;
    T mMapFeature;
    GeoColor mGeoColor;
    int mColor;

    public FeatureMapperBase(){
        mMap = application.getGoogleMap();
        reset();
        mGeoColor = new GeoColor();
    }

    public abstract IFeatureMapper draw(Feature feature);

    public abstract IFeatureMapper addStyle(Style style);

    public void setLegendIcon(LegendLayer layer){
        Drawable newDrawable = mGeoColor.changeColor(layer.getLegendIcon(), mColor);

        layer.setLegendIcon(newDrawable);
        layer.setImageSrc();
    }

    public abstract void reset();
}
