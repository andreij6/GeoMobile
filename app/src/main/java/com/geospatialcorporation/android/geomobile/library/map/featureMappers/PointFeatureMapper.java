package com.geospatialcorporation.android.geomobile.library.map.featureMappers;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class PointFeatureMapper extends FeatureMapperBase<MarkerOptions> {

    @Override
    public IFeatureMapper draw(Feature feature) {
        mMapFeature.position(getLatLng(feature));

        mMapFeature.flat(true);

        return this;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {

        Drawable d = application.getAppContext().getDrawable(R.drawable.ic_checkbox_blank_circle_black_18dp);
        int fillColor = mGeoColor.parseColor(style.getFillColor());

        Bitmap iconBitmap;

        if(d instanceof BitmapDrawable){
            iconBitmap = ((BitmapDrawable)d).getBitmap();

            Bitmap coloredBitmap = mGeoColor.changeColor(iconBitmap, fillColor);

            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(coloredBitmap);

            mMapFeature.icon(icon);
        }

        return this;
    }

    @Override
    public void commit() {
        mMap.addMarker(mMapFeature);
    }

    @Override
    public void reset() {
        mMapFeature = new MarkerOptions();
    }

    protected LatLng getLatLng(Feature feature) {
        return feature.getGeometry().getLatLng();
    }

}
