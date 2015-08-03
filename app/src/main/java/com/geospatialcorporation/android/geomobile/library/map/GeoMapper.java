package com.geospatialcorporation.android.geomobile.library.map;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
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
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class GeoMapper implements IGeoMapper  {
    private static final String TAG = GeoMapper.class.getSimpleName();

    HashMap<Integer, IFeatureMapper> mStrategies;

    public GeoMapper(){
        setStrategies();
    }

    protected void setStrategies() {
        mStrategies = new HashMap<>();

        mStrategies.put(GeometryTypeCodes.Point, new PointFeatureMapper());
        mStrategies.put(GeometryTypeCodes.MultiPoint, new MultiPointFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Line, new LineFeatureMapper());
        mStrategies.put(GeometryTypeCodes.MultiLine, new MultiLineFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Polygon, new PolygonFeatureMapper());
        mStrategies.put(GeometryTypeCodes.MultiPolygon, new MultiPolygonFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Extent, new ExtentFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Collection, new CollectionFeatureMapper());
        mStrategies.put(GeometryTypeCodes.Raster, new RasterFeatureMapper());
    }

    @Override
    public void map(List<MapQueryResponse> responses, LegendLayer llayer) {
        new MapFeaturesTask(responses, llayer).execute();
    }

    //region task attempt

    protected class MapFeaturesTask extends AsyncTask<Void, Void, Integer>{

        public MapFeaturesTask(List<MapQueryResponse> responses, LegendLayer llayer){
            mResponses = responses;
            mLlayer = llayer;
        }

        //region properties
        ProgressDialogHelper mProgressHelper;
        LegendLayer mLlayer;
        List<MapQueryResponse> mResponses;
        IFeatureMapper mMapper;
        //endregion

        @Override
        protected void onPreExecute() {
            mProgressHelper = new ProgressDialogHelper(application.getMainActivity());
            mProgressHelper.toggleProgressDialog();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int result = 1;
            try {
                int geometryTypeCode = mResponses.get(0).getFeatures().get(0).getGeometry().getGeometryTypeCode();

                mMapper = mStrategies.get(geometryTypeCode);

                for (MapQueryResponse response : mResponses) {

                    List<Feature> FeaturesList = response.getFeatures();

                    for (Feature feature : FeaturesList) {

                        mMapper.reset();

                        mMapper.draw(feature).addStyle(response.getStyle()).commit(mLlayer);
                    }
                }

                result = 1;
            } catch (Exception e){
                Log.d(TAG, e.getMessage());

                result = 2;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mProgressHelper.toggleProgressDialog();

            if(integer == 1) {

                mLlayer.setLegendIcon(mMapper.getActiveDrawable());
                mLlayer.setImageSrc();

                application.getLayerManager().showLayers();
            }
        }
    }

    //endregion
}
