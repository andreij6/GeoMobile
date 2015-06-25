package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.CollectionFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.ExtentFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.IFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.LineFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.PointFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.PolygonFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.RasterFeatureMapper;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.MapQueryResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class GeoMapper implements IGeoMapper  {

    HashMap<Integer, IFeatureMapper> mStrategies;

    public GeoMapper(){
        setStrategies();
    }

    protected void setStrategies() {
        mStrategies = new HashMap<>();

        mStrategies.put(GeometryTypeCodes.Point, new PointFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Line, new LineFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Polygon, new PolygonFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Extent, new ExtentFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Collection, new CollectionFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Raster, new RasterFeatureMapper());
    }

    @Override
    public void map(List<MapQueryResponse> responses) {
        for(MapQueryResponse response : responses){
            for(Feature feature : response.getFeatures()){

                IFeatureMapper mapper = mStrategies.get(feature.getGeometry().getGeometryTypeCode());

                mapper.reset();

                mapper.draw(feature)
                        .addStyle(response.getStyle())
                        .commit();
            }
        }
    }
}
