package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by andre on 6/30/2015.
 */
public class PolygonOptionsManager extends OptionsManagerBase<PolygonOptions, Polygon> {
    private static final String TAG = PolygonOptionsManager.class.getSimpleName();

    GoogleMap mMap;

    public void showLayers(GoogleMap map) {
        mMap = map;
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(map, bounds).execute();
    }

    @Override
    protected void removeMapObject(UUID key) {
        if(mVisibleLayers.get(key) != null) {
            mVisibleLayers.get(key).remove();
            mVisibleLayers.remove(key);
        }
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, PostParameters>{

        GoogleMap mGoogleMap;
        LatLngBounds mBounds;

        public ShowLayersAsync(GoogleMap map, LatLngBounds bounds){
            mGoogleMap = map;
            mBounds = bounds;
        }

        @Override
        protected PostParameters doInBackground(Void... params) {
            PostParameters result = new PostParameters();

            List<HashMap<UUID, OptionFeature<PolygonOptions>>> CachedOptions = getOption();

            for (HashMap<UUID, OptionFeature<PolygonOptions>> cachedOptions : CachedOptions) {
                if(cachedOptions != null) {
                    for (Map.Entry<UUID, OptionFeature<PolygonOptions>> entry : cachedOptions.entrySet()) {


                        UUID key = entry.getKey();
                        PolygonOptions option = entry.getValue().getOption();
                        Boolean pointInBounds = false;

                        for(LatLng point : option.getPoints()) {
                            if (mBounds.contains(point)) {
                                if (!mVisibleLayers.containsKey(key)) {

                                    FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                    result.addPolygon(option, featureInfo, key);
                                }

                                pointInBounds = true;
                                break;
                            }
                        }

                        if(!pointInBounds){
                            result.remove(key);
                        }
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(PostParameters options) {
            if(contentFragmentIsGoogleMapFragment()) {
                options.mapFeatures();
            }
        }

    }

    protected boolean contentFragmentIsGoogleMapFragment() {
        return application.getMainActivity().getContentFragment() instanceof GoogleMapFragment;
    }

    protected class PostParameters {

        List<PolygonFeature> mPolygonFeatures;
        List<UUID> mPolygonsToRemove;

        public PostParameters(){
            mPolygonFeatures = new ArrayList<>();
            mPolygonsToRemove = new ArrayList<>();
        }

        public void mapFeatures(){
            for (PolygonFeature pparams: mPolygonFeatures) {

                Polygon polygon = mMap.addPolygon(pparams.getOptions());
                mIdFeatureIdMap.put(polygon.getId(), pparams.getFeatureInfo());
                mVisibleLayers.put(pparams.getKey(), polygon);
            }

            for(UUID key : mPolygonsToRemove){
                removeMapObject(key);
            }
        }

        public void addPolygon(PolygonOptions options, FeatureInfo info, UUID key){
            mPolygonFeatures.add(new PolygonFeature(options, info, key));
        }

        public void remove(UUID key){
            mPolygonsToRemove.add(key);
        }
    }

    protected class PolygonFeature {
        PolygonOptions mOptions;
        FeatureInfo mFeatureInfo;
        UUID mKey;

        public PolygonFeature(PolygonOptions option, FeatureInfo featureInfo, UUID key) {
            mOptions = option;
            mFeatureInfo = featureInfo;
            mKey = key;
        }

        public UUID getKey() {
            return mKey;
        }

        public PolygonOptions getOptions() {
            return mOptions;
        }

        public FeatureInfo getFeatureInfo() {
            return mFeatureInfo;
        }
    }


}
