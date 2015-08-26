package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by andre on 6/30/2015.
 */
public class PolylineOptionsManager extends OptionsManagerBase<PolylineOptions, Polyline> {

    private static final String TAG = Polyline.class.getSimpleName();

    public void showLayers(GoogleMap map) {
        new ShowLayersAsync(map).execute();
    }

    @Override
    public void removeMapObject(UUID key){
        mVisibleLayers.get(key).remove();
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, List<PostParameters>> {

        GoogleMap mGoogleMap;

        public ShowLayersAsync(GoogleMap map){
            mGoogleMap = map;
        }

        @Override
        protected List<PostParameters> doInBackground(Void... params) {
            List<PostParameters> result = new ArrayList<>();

            List<HashMap<UUID, OptionFeature<PolylineOptions>>> CachedOptions = getOption();

            for (HashMap<UUID, OptionFeature<PolylineOptions>> cachedOptions : CachedOptions) {
                for (Map.Entry<UUID, OptionFeature<PolylineOptions>> entry : cachedOptions.entrySet()) {

                    UUID key = entry.getKey();

                    if (!mVisibleLayers.containsKey(key)) {

                        PolylineOptions option = entry.getValue().getOption();
                        FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                        result.add(new PostParameters(option, featureInfo, key));

                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<PostParameters> options) {
            if(contentFragmentIsGoogleMapFragment()) {
                for (PostParameters pparams: options) {

                    Polyline polyline = mGoogleMap.addPolyline(pparams.getOptions());
                    mIdFeatureIdMap.put(polyline.getId(), pparams.getFeatureInfo());
                    mVisibleLayers.put(pparams.getKey(), polyline);
                }
            }
        }
    }

    protected boolean contentFragmentIsGoogleMapFragment() {
        return application.getMainActivity().getContentFragment() instanceof GoogleMapFragment;
    }

    protected class PostParameters {

        PolylineOptions mOptions;
        FeatureInfo mFeatureInfo;
        UUID mKey;

        public PostParameters(PolylineOptions option, FeatureInfo featureInfo, UUID key) {
            mOptions = option;
            mFeatureInfo = featureInfo;
            mKey = key;
        }

        public UUID getKey() {
            return mKey;
        }

        public PolylineOptions getOptions() {
            return mOptions;
        }

        public FeatureInfo getFeatureInfo() {
            return mFeatureInfo;
        }
    }
}
