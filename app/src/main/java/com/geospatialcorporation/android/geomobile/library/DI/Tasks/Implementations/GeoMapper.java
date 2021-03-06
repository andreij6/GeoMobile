package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IMapFeaturesTask;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.constants.PluginCodes;
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
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

import java.util.HashMap;
import java.util.List;

public class GeoMapper implements IMapFeaturesTask, IPostExecuter<Integer> {

    private static final String TAG = MapFeaturesTask.class.getSimpleName();

    HashMap<Integer, IFeatureMapper> mStrategies;
    ILayerManager mLayerManager;
    LegendLayer mLlayer;
    IFeatureMapper mMapper;
    ILayerStyleTask mLayerStyleTask;


    public GeoMapper(){
        setupStrategies();
        mLayerManager = application.getMapComponent().provideLayerManager();
        mLayerStyleTask = application.getTasksComponent().provideLayerStyleTask();

    }

    protected void setupStrategies(){
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
    public void mapFeatures(List<MapQueryResponse> responses, LegendLayer legendLayer) {
        mLlayer = legendLayer;

        if(legendLayer.getLayer().getPluginId() == PluginCodes.VideoLayers){
            mMapper = mStrategies.get(GeometryTypeCodes.Line);

            //new MapVideoLineTask(responses, legendLayer).execute();
        }

        mMapper = mStrategies.get(legendLayer.getLayer().getGeometryTypeCodeId());
        new MapFeaturesTask(responses, legendLayer).execute();
    }

    @Override
    public void onPostExecute(Integer integer) {
        if(application.getEditingLayerMode()){
            mLayerManager.editLayers(mLlayer);
        } else {
            mLayerManager.showLayer(mLlayer);
        }

        if (mLlayer.getCheckBox() != null) {
            mLlayer.getCheckBox().setEnabled(true);
        }
    }


    protected class MapFeaturesTask extends GeoAsyncTask<Void, Integer, Integer> {

        public MapFeaturesTask(List<MapQueryResponse> responses, LegendLayer llayer){
            super(GeoMapper.this);
            mResponses = responses;
            mLlayer = llayer;
        }

        //region properties
        List<MapQueryResponse> mResponses;
        //endregion

        @Override
        protected Integer doInBackground(Void... params) {
            int result;

            try {
                if(mResponses != null && mResponses.size() > 0){

                    mMapper.setLegendLayer(mLlayer);

                    for (MapQueryResponse response : mResponses) {

                        List<Feature> FeaturesList = response.getFeatures();

                        //Integer total = FeaturesList.size();
                        //Integer counter = 0;

                        for (Feature feature : FeaturesList) {

                            //publishProgress((int)((counter / (float) total) * 100));

                            mMapper.reset();

                            mMapper.draw(feature)
                                    .addStyle(response.getStyle())
                                    .commit(mLlayer);

                            //counter++;
                        }
                    }


                }

                result = 1;
            } catch (Exception e){

                result = 2;
            }

            return result;
        }
    }
}
