package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;
import com.google.android.gms.maps.GoogleMap;

public abstract class FeatureMapperBase<T> implements IFeatureMapper {
    private static final String TAG = FeatureMapperBase.class.getSimpleName();

    GoogleMap mMap;
    T mMapFeature;
    GeoColor mGeoColor;
    int mColor;
    protected  LegendLayer mLegendLayer;


    public Drawable getActiveDrawable() {
        return mActiveDrawable;
    }

    public void setActiveDrawable(Drawable activeDrawable) {
        mActiveDrawable = activeDrawable;
    }

    Drawable mActiveDrawable;

    public FeatureMapperBase(){
        mMap = application.getGoogleMap();
        reset();
        mGeoColor = new GeoColor();
        mColor = 0;
    }

    public abstract IFeatureMapper draw(Feature feature);

    public abstract IFeatureMapper addStyle(Style style);

    public void setLegendIcon(LegendLayer layer){
        if(mActiveDrawable == null) {
            setActiveDrawable(layer.getLegendIcon());
        }

    }

    public abstract void reset();

    public void setLegendLayer(LegendLayer layer){
        mLegendLayer = layer;
    }
}
