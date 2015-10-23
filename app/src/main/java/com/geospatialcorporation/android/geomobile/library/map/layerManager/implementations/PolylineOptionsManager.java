package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

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
import com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments.TabletMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolylineOptionsManager extends OptionsManagerBase<PolylineOptions, Polyline> {

    private static final String TAG = Polyline.class.getSimpleName();
    GoogleMap mMap;
    IMapStatusBarManager mMapStatusBarManager;

    public PolylineOptionsManager(){
        mMapStatusBarManager = application.getStatusBarManager();
    }

    public void showLayers(GoogleMap map) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        mMap = map;
        new ShowLayersAsync(map, bounds, null).execute();
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqeId) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        mMap = map;
        new ShowLayersAsync(map, bounds, uniqeId).execute();
    }

    @Override
    public void getNextFeature(String featureId, int layerId, boolean isNext) {
        HashMap<UUID, OptionFeature<PolylineOptions>> optionFeatures = mLayerOptions.get(layerId);

        Collection<OptionFeature<PolylineOptions>> values = optionFeatures.values();

        List<OptionFeature<PolylineOptions>> valuesList = new ArrayList<>(values);

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

                final Iterable<Polyline> polygons = getShowingLayers();

                final PolylineOptions selected = valuesList.get(next).getOption();

                LatLngBounds bounds = getBounds(selected.getPoints());


                if(!application.getIsTablet()) {
                    final GoogleMapFragment mapFragment = (GoogleMapFragment) application.getMainActivity().getContentFragment();

                    mapFragment.centerMap(bounds.getCenter());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (Polyline ss : polygons) {
                                if (ss.getPoints().equals(selected.getPoints())) {
                                    mapFragment.getFeatureWindow(ss.getId(), LayerManager.LINE);

                                    mapFragment.highlight(ss);
                                    break;
                                }
                            }
                        }
                    }, 1100);
                } else {
                    final TabletMapFragment mapFragment = application.getMainTabletActivity().getMapFragment();

                    mapFragment.centerMap(bounds.getCenter());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (Polyline ss : polygons) {
                                if (ss.getPoints().equals(selected.getPoints())) {
                                    mapFragment.getFeatureWindow(ss.getId(), LayerManager.LINE);

                                    mapFragment.highlight(ss);
                                    break;
                                }
                            }
                        }
                    }, 1100);
                }


            }
        }
    }

    @Override
    public void removeMapObject(UUID key){
        try {
            if (mVisibleLayers.get(key) != null) {
                mVisibleLayers.get(key).remove();
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    //region ShowLayers
    protected class ShowLayersAsync extends AsyncTask<Void, Void, PostParameters> {

        GoogleMap mGoogleMap;
        LatLngBounds mBounds;
        PostParameters mResult;
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
             mResult = new PostParameters();

            List<HashMap<UUID, OptionFeature<PolylineOptions>>> CachedOptions = getOption();

            for (HashMap<UUID, OptionFeature<PolylineOptions>> cachedOptions : CachedOptions) {

                if(cachedOptions != null) {

                    for (Map.Entry<UUID, OptionFeature<PolylineOptions>> entry : cachedOptions.entrySet()) {

                        UUID key = entry.getKey();
                        PolylineOptions option = entry.getValue().getOption();
                        Boolean pointInBounds = false;



                        if (GeoPolyUtil.BoundsIntersect(option.getPoints(), mBounds)) {
                            if (!mVisibleLayers.containsKey(key)) {

                                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                mResult.addPolyline(option, featureInfo, key);

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

                        if (!pointInBounds) {
                            mResult.remove(key);
                        }
                    }
                }

            }

            return mResult;
        }

        @Override
        protected void onPostExecute(PostParameters options) {
            IMapStatusBarManager mapStatusBarManager = application.getStatusBarManager();

            if(contentFragmentIsGoogleMapFragment() || application.getIsTablet()) {
                options.mapFeatures();

                if(mUUID != null) {
                    mapStatusBarManager.finished(StatusBarManagerBase.LINES, mUUID);
                }
            }


        }
    }

    protected boolean contentFragmentIsGoogleMapFragment() {
        if(application.getIsTablet()){
            return false;
        }else {
            return application.getMainActivity().getContentFragment() instanceof GoogleMapFragment;
        }
    }

    protected class PostParameters {

        List<PolylineFeatures> mFeatures;
        List<UUID> mLinesToRemove;

        public PostParameters(){
            mFeatures = new ArrayList<>();
            mLinesToRemove = new ArrayList<>();
        }

        public void mapFeatures() {
            for (PolylineFeatures pparams: mFeatures) {
                Polyline polyline = mMap.addPolyline(pparams.getOptions());

                mIdFeatureIdMap.put(polyline.getId(), pparams.getFeatureInfo());
                mVisibleLayers.put(pparams.getKey(), polyline);
            }

            mStatusBarManager.FinishLoading(GeometryTypeCodes.Line);

            for(UUID key : mLinesToRemove){
                removeMapObject(key);
                mVisibleLayers.remove(key);
            }



        }

        public void addPolyline(PolylineOptions option, FeatureInfo featureInfo, UUID key) {
            mFeatures.add(new PolylineFeatures(option, featureInfo, key));
        }

        public void remove(UUID key) {
            mLinesToRemove.add(key);
        }

        public boolean hasFeatures() {
            return mFeatures.size() > 0;
        }
    }

    protected class PolylineFeatures {
        PolylineOptions mOptions;
        FeatureInfo mFeatureInfo;
        UUID mKey;

        public PolylineFeatures(PolylineOptions option, FeatureInfo featureInfo, UUID key){
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
    //endregion

    //region Attempt
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

            HashMap<Integer, Double> distances = new HashMap<>();

            List<HashMap<UUID, OptionFeature<PolylineOptions>>> CachedOptions = getOption();
            int counter = 0;
            for (HashMap<UUID, OptionFeature<PolylineOptions>> cachedOptions : CachedOptions) {

                if (cachedOptions != null) {
                    for (Map.Entry<UUID, OptionFeature<PolylineOptions>> entry : cachedOptions.entrySet()) {
                        PolylineOptions option = entry.getValue().getOption();

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
                                closestPos = getMidPoint(option.getPoints().get(0), option.getPoints().get(1));
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
                                furthestPos= getMidPoint(option.getPoints().get(0), option.getPoints().get(1));
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

        public LatLng getMidPoint(LatLng point1, LatLng point2){
            double dLon = Math.toRadians(point2.longitude - point1.longitude);

            //convert to radians
            double lat1 = Math.toRadians(point1.latitude);
            double lat2 = Math.toRadians(point2.latitude);
            double lon1 = Math.toRadians(point1.longitude);

            double Bx = Math.cos(lat2) * Math.cos(dLon);
            double By = Math.cos(lat2) * Math.sin(dLon);
            double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
            double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

            //print out in degrees
            return new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));
        }


        public double rad(double x){
            return x * Math.PI / 180;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);

            GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

            mapFragment.simulateClick(latLng);
        }
    }
    //endregion

    private LatLngBounds getBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng point : points) {
            builder.include(point);
        }

        return builder.build();
    }
}
