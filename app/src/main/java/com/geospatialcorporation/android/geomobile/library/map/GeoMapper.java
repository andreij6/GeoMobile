package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.CollectionFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.ExtentFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.IFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.LineFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.MultiLineFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.MultiPointFeatureMapper;
import com.geospatialcorporation.android.geomobile.library.map.featureMappers.MultiPolygonFeatureMapper;
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
        mStrategies = new HashMap<>();
        setStrategies();
    }

    protected void setStrategies() {
        mStrategies.put(GeometryTypeCodes.Point, new PointFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Line, new LineFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Polygon, new PolygonFeatureMapper());
        mStrategies.put(GeometryTypeCodes.MultiLine, new MultiLineFeatureMapper());
        mStrategies.put(GeometryTypeCodes.MultiPoint, new MultiPointFeatureMapper());
        mStrategies.put(GeometryTypeCodes.MultiPolygon, new MultiPolygonFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Extent, new ExtentFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Collection, new CollectionFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Raster, new RasterFeatureMapper());
    }

    @Override
    public void map(List<MapQueryResponse> responses) {
        for(MapQueryResponse response : responses){
            for(Feature feature : response.getFeatures()){

                IFeatureMapper mapper = mStrategies.get(feature.getGeometry().getGeometryTypeCode());

                mapper.draw(feature);
            }
        }
    }
}
