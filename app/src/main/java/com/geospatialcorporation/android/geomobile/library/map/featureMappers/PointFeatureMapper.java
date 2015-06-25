package com.geospatialcorporation.android.geomobile.library.map.featureMappers;


import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class PointFeatureMapper extends FeatureMapperBase<MarkerOptions> {

    public PointFeatureMapper(){
        reset();
    }

    @Override
    public IFeatureMapper draw(Feature feature) {
        mMapFeature.position(getLatLng(feature));

        mMapFeature.flat(true);

        return this;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
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
