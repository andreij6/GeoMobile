package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.PointStyleCodes;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by andre on 6/25/2015.
 */
public abstract class PointFeatureMapperBase extends SingleFeatureMapperBase<MarkerOptions> {

    BitmapDescriptor mIcon;

    @Override
    public void drawFeature(Geometry geometry, MarkerOptions options) {
        options.position(getLatLng(geometry));
        options.anchor(0.5f, 0.5f);
        options.flat(true);
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        addStyles(mMapFeature, style);

        return this;
    }

    @Override
    public int addStyles(MarkerOptions options, Style style) {
        if(mIcon == null) {
            Drawable d = setDrawable(style);

            mColor = mGeoColor.parseColor(style.getFillColor());

            Bitmap iconBitmap = ((BitmapDrawable) d).getBitmap();

            Bitmap coloredBitmap = mGeoColor.changeColor(iconBitmap, mColor);

            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(coloredBitmap);

            mIcon = icon;

            options.icon(icon);
        } else  {
            options.icon(mIcon);
        }

        return mColor;
    }

    @Override
    public void commit(LegendLayer layer) {
        addMapObject(layer, mMapFeature);

        setLegendIcon(layer);
    }

    @Override
    public void addMapObject(LegendLayer layer, MarkerOptions option) {
        mLayerManager.addPoint(layer.getLayer().getId(), option, new FeatureInfo(layer.getLayer().getId(), mFeatureId));

    }

    @Override
    public void reset() {
        mMapFeature = new MarkerOptions();
    }

    //region
    protected Drawable setDrawable(Style style) {
        Drawable d = null;

        switch (style.getPointStyleCode()) {
            case PointStyleCodes.CIRCLE:
                d = application.getAppContext().getDrawable(R.drawable.ic_checkbox_blank_circle_black_18dp);
                break;
            case PointStyleCodes.DIAMOND:
                d = application.getAppContext().getDrawable(R.drawable.ic_checkbox_blank_black_18dp);  //TODO: find a Diamond
                break;
            case PointStyleCodes.SQUARE:
                d = application.getAppContext().getDrawable(R.drawable.ic_checkbox_blank_black_18dp);
                break;
            default:
                break;
        }

        return d;
    }

    protected LatLng getLatLng(Geometry geom) {
        return geom.getLatLng();
    }
}
