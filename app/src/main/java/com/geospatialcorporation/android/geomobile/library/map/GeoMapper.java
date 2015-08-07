package com.geospatialcorporation.android.geomobile.library.map;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
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

    protected class MapFeaturesTask extends AsyncTask<Void, Integer, Integer>{

        public MapFeaturesTask(List<MapQueryResponse> responses, LegendLayer llayer){
            mResponses = responses;
            mLlayer = llayer;
        }

        //region properties
        LegendLayer mLlayer;
        List<MapQueryResponse> mResponses;
        IFeatureMapper mMapper;
        Integer mProgressStatus;
        ProgressBar mProgressBar;
        CheckBox mCheckBox;
        //endregion


        @Override
        protected void onPreExecute() {
            mProgressBar = mLlayer.getProgressBar();
            mProgressStatus = 0;
            mProgressBar.setProgress(mProgressStatus);
            mCheckBox = mLlayer.getCheckBox();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int result = 1;
            try {
                if(mResponses != null){
                    int geometryTypeCode = mResponses.get(0).getFeatures().get(0).getGeometry().getGeometryTypeCode();

                    mMapper = mStrategies.get(geometryTypeCode);



                    for (MapQueryResponse response : mResponses) {

                        List<Feature> FeaturesList = response.getFeatures();

                        Integer total = FeaturesList.size();
                        Integer counter = 0;

                        for (Feature feature : FeaturesList) {
                            publishProgress((int)((counter / (float) total) * 100));

                            mMapper.reset();

                            mMapper.draw(feature).addStyle(response.getStyle()).commit(mLlayer);

                            counter++;
                        }


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
        protected void onProgressUpdate(Integer... progress) {
            mProgressBar.setProgress(progress[0]);
            Log.d(TAG, progress[0] + "");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1) {

                mLlayer.setLegendIcon(mMapper.getActiveDrawable());
                mLlayer.setImageSrc();
                mLlayer.getProgressBar().setVisibility(View.GONE);

                application.getLayerManager().showLayers();
            } else {
                mProgressBar.setVisibility(View.GONE);
            }

            mCheckBox.setEnabled(true);
        }
    }

    //endregion
}
