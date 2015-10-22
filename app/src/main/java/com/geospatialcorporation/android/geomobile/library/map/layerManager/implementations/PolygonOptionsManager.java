package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager.StatusBarManagerBase;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.library.util.GeoPolyUtil;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolygonOptionsManager extends OptionsManagerBase<PolygonOptions, Polygon> {
    private static final String TAG = PolygonOptionsManager.class.getSimpleName();

    GoogleMap mMap;
    IMapStatusBarManager mMapStatusBarManager;

    public PolygonOptionsManager(){
        mMapStatusBarManager = application.getStatusBarManager();
    }

    public void showLayers(GoogleMap map) {
        mMap = map;
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(map, bounds, null).execute();
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqueId) {
        mMap = map;
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(map, bounds, uniqueId).execute();
    }

    @Override
    public void getNextFeature(String featureId, int layerId, boolean isNext) {
        HashMap<UUID, OptionFeature<PolygonOptions>> optionFeatures = mLayerOptions.get(layerId);

        Collection<OptionFeature<PolygonOptions>> values = optionFeatures.values();

        List<OptionFeature<PolygonOptions>> valuesList = new ArrayList<>(values);

        Collections.sort(valuesList);

        for (int i = 0; i < valuesList.size(); i++) {

            if(valuesList.get(i).getFeatureInfo().getFeatureId().equals(featureId)){

                int next = 0;

                if(isNext){
                    next = i + 1;
                } else {
                    next = i - 1;
                }

                if(next < 0){
                    next = valuesList.size() - 1;
                }

                if(next >= valuesList.size()){
                    next = 0;
                }

                final GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

                final Iterable<Polygon> polygons = getShowingLayers();

                final PolygonOptions selected = valuesList.get(next).getOption();

                LatLngBounds bounds = getBounds(selected.getPoints());

                mapFragment.centerMap(bounds.getCenter());

                new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        for (Polygon ss : polygons) {
                            if(ss.getPoints().equals(selected.getPoints())){
                                mapFragment.getFeatureWindow(ss.getId(), LayerManager.POLYGON);

                                mapFragment.highlight(ss);
                                break;
                            }
                        }
                    }
                }, 2000);



            }

        }
    }

    @Override
    protected void removeMapObject(UUID key) {
        try {
            if (mVisibleLayers.get(key) != null) {
                mVisibleLayers.get(key).remove();
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    //region ShowLayers
    protected class ShowLayersAsync extends AsyncTask<Void, Void, PostParameters>{

        GoogleMap mGoogleMap;
        LatLngBounds mBounds;
        UUID mUUID;
        Boolean mStatusBarVisible;

        public ShowLayersAsync(GoogleMap map, LatLngBounds bounds, UUID uniqueId){
            mGoogleMap = map;
            mBounds = bounds;
            mUUID = uniqueId;
            mStatusBarVisible = false;
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

                        if (GeoPolyUtil.BoundsIntersect(option.getPoints(), mBounds)) {
                            if (!mVisibleLayers.containsKey(key)) {

                                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                result.addPolygon(option, featureInfo, key);

                                //getActivity().runOnUiThread(new Runnable() {
                                //    @Override
                                //    public void run() {
                                //        if (!mStatusBarVisible && mUUID != null) {
                                //            mMapStatusBarManager.ensureStatusBarVisible();
                                //            mStatusBarVisible = true;
                                //        }
                                //    }
                                //});
                            }

                            pointInBounds = true;
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
            IMapStatusBarManager mapStatusBarManager = application.getStatusBarManager();

            if(contentFragmentIsGoogleMapFragment() || application.getIsTablet()) {
                options.mapFeatures();

                if(mUUID != null) {

                    mapStatusBarManager.finished(StatusBarManagerBase.POLYGONS, mUUID);
                }
            }
        }

    }

    protected boolean contentFragmentIsGoogleMapFragment() {
        if(application.getMainActivity() == null){
            return false;
        }else {
            return application.getMainActivity().getContentFragment() instanceof GoogleMapFragment;
        }
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

            mStatusBarManager.FinishLoading(GeometryTypeCodes.Polygon);

            for(UUID key : mPolygonsToRemove){
                removeMapObject(key);
                mVisibleLayers.remove(key);
            }
        }

        public void addPolygon(PolygonOptions options, FeatureInfo info, UUID key){
            mPolygonFeatures.add(new PolygonFeature(options, info, key));
        }

        public void remove(UUID key){
            mPolygonsToRemove.add(key);
        }

        public boolean hasFeatures() {
            return mPolygonFeatures.size() > 0;
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
    //endregion

    //region Attempt
    /*
    protected class GetNextFeatureAsync extends AsyncTask<Void, Void, LatLng>{

        LatLng mHighlightedCenter;

        public GetNextFeatureAsync(LatLng center){
            mHighlightedCenter = center;

        }

        @Override
        protected LatLng doInBackground(Void... params) {
            int Radius = 6371;
            int closest = -1;
            int furthest = -1;
            LatLng closestPos = null;
            LatLng furthestPos = null;

            if(application.getIsTablet()){
                return null;
            }

            HashMap<Integer, Double> distances = new HashMap<>();

            List<HashMap<UUID, OptionFeature<PolygonOptions>>> CachedOptions = getOption();
            int counter = 0;
            for (HashMap<UUID, OptionFeature<PolygonOptions>> cachedOptions : CachedOptions) {

                if (cachedOptions != null) {
                    for (Map.Entry<UUID, OptionFeature<PolygonOptions>> entry : cachedOptions.entrySet()) {
                        PolygonOptions option = entry.getValue().getOption();

                        LatLng pos = getBounds(option.getPoints()).getCenter();

                        if(pos.longitude == mHighlightedCenter.longitude && pos.latitude == mHighlightedCenter.latitude){
                            continue;
                        }

                        if(pos.longitude > mHighlightedCenter.longitude) {
                            double dLat = rad(pos.latitude - mHighlightedCenter.latitude);
                            double dLong = rad(pos.longitude - mHighlightedCenter.longitude);

                            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(mHighlightedCenter.latitude)) * Math.cos(rad(mHighlightedCenter.latitude))
                                    * Math.sin(dLong / 2) * Math.sin(dLong / 2);

                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                            double d = Radius * c;

                            distances.put(counter, d);

                            if (closest == -1 || d < distances.get(closest)) {
                                closest = counter;
                                List<LatLng> points = option.getPoints();
                                closestPos = getMidPoint(points.get(0), points.get(1), points.get(2));
                            }
                        } else {
                            double dLat = rad(pos.latitude - mHighlightedCenter.latitude);
                            double dLong = rad(pos.longitude - mHighlightedCenter.longitude);

                            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(mHighlightedCenter.latitude)) * Math.cos(rad(mHighlightedCenter.latitude))
                                    * Math.sin(dLong / 2) * Math.sin(dLong / 2);

                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                            double d = Radius * c;

                            distances.put(counter, d);

                            if (furthest == -1 || d > distances.get(furthest)) {
                                furthest = counter;
                                List<LatLng> points = option.getPoints();
                                furthestPos = getMidPoint(points.get(0), points.get(1), points.get(2));
                            }
                        }

                        counter++;
                    }
                }
            }

            if(closestPos != null){
                return closestPos;
            } else {
                if(furthestPos != null){
                    return furthestPos;
                }

                return null;
            }
        }

        public double rad(double x){
            return x * Math.PI / 180;
        }

        public LatLng getMidPoint(LatLng point1, LatLng point2, LatLng point3){
            LatLngBounds.Builder builder = LatLngBounds.builder();

            builder.include(point1);
            builder.include(point2);
            builder.include(point3);

            LatLngBounds bounds = builder.build();

            return bounds.getCenter();
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);

            GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

            Polygon mHighlighed = mapFragment.getHighlightedPolygon();

            Iterable<Polygon> polygons = getShowingLayers();

            for (Polygon ss : polygons) {

                if (PolyUtil.containsLocation(latLng, ss.getPoints(), true) && !ss.getPoints().equals(mHighlighed.getPoints())) {
                    mapFragment.getFeatureWindow(ss.getId(), LayerManager.POLYGON);

                    mapFragment.highlight(ss);
                    break;
                }

            }

            //mapFragment.simulateClick(latLng);
        }
    }
    */
    //endregion

    private LatLngBounds getBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng point : points) {
            builder.include(point);
        }

        return builder.build();
    }


}
