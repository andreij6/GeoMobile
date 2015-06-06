package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.SlidingPanelAction;
import com.geospatialcorporation.android.geomobile.library.helpers.MarkerComparer;
import com.geospatialcorporation.android.geomobile.library.helpers.QueryMachine;
import com.geospatialcorporation.android.geomobile.library.helpers.QuerySenderHelper;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.models.Query.point.Max;
import com.geospatialcorporation.android.geomobile.models.Query.point.Min;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 6/6/2015.
 */
public class QueryMode implements IViewMode {

    SlidingUpPanelLayout mPanel;
    HashMap<Integer, FloatingActionButton> mControls;
    GoogleMap mMap;
    OnFragmentInteractionListener mListener;
    Builder mBuilder;

    public QueryMode(Builder builder){
        mPanel = builder.mPanel;
        mControls = builder.mControls;
        mMap = builder.mMap;
        mListener = builder.mListener;
        mBuilder = builder;
    }

    @Override
    public void Disable() {
        mBuilder.Reset();
    }

    /*
    @Override
    public void ShouldDisable(Integer panelCallback) {
        if(panelCallback == SlidingPanelAction.PANEL_ONSLIDE
                || panelCallback == SlidingPanelAction.PANEL_ANCORED
                || panelCallback == SlidingPanelAction.PANEL_COLLAPSED
                || panelCallback == SlidingPanelAction.PANEL_EXPANDED)
        {
            Disable();
        }
    }
    **/

    public static class Builder{

        SlidingUpPanelLayout mPanel;
        HashMap<Integer, FloatingActionButton> mControls;
        GoogleMap mMap;
        FragmentActivity mActivity;
        OnFragmentInteractionListener mListener;
        QueryMachine mQueryMachine;

        Polygon mShape;
        List<Marker> mMarkers = new ArrayList<>();
        public static final int BOX = 2;

        public static final Integer BOXQUERYBTN = 1;
        public static final Integer POINTQUERYBTN = 2;
        public static final Integer CLOSEBTN = 3;

        public Builder setControls(FloatingActionButton boxQueryBtn, FloatingActionButton pointQueryBtn, FloatingActionButton closeBtn) {

            List<FloatingActionButton> controls = Arrays.asList(boxQueryBtn, pointQueryBtn, closeBtn);

            SetVisibility(controls, View.VISIBLE);

            mQueryMachine = new QueryMachine();

            SetClickEvents(boxQueryBtn, pointQueryBtn, closeBtn);

            mControls = new HashMap<>();
            
            mControls.put(BOXQUERYBTN, boxQueryBtn);
            mControls.put(POINTQUERYBTN, pointQueryBtn);
            mControls.put(CLOSEBTN, closeBtn);

            return this;
        }

        protected Builder SetClickEvents(FloatingActionButton boxQueryBtn, FloatingActionButton pointQueryBtn, FloatingActionButton closeBtn) {
            boxQueryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mQueryMachine.setState(QueryMachine.State.BOXQUERY_INPROGRESS);

                    clearMapQueryPoints();
                    SetTitle(R.string.box_query);

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng ll) {
                            boxQueryStep2AddPoint(ll.latitude, ll.longitude);
                        }
                    });
                }
            });

            pointQueryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQueryMachine.setState(QueryMachine.State.POINTQUERY_INPROGRESS);

                    clearMapQueryPoints();
                    SetTitle(R.string.point_query);

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            setPointMarker(latLng.latitude, latLng.longitude);
                        }
                    });
                }
            });

            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ShouldOnlyClear()) {

                        clearMapQueryPoints();

                        UpdateState();
                    } else {

                        if (ShouldReset()) {

                            Reset();
                        }
                    }
                }
            });

            return this;
        }

        public Builder setupUI(SlidingUpPanelLayout upUI) {
            mPanel = upUI;

            if(upUI != null){
                upUI.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }

            return this;
        }

        public Builder setDependents(GoogleMap map, OnFragmentInteractionListener listener, FragmentActivity activity) {
            mMap = map;
            mListener = listener;
            mActivity = activity;
            return this;
        }

        public QueryMode create(){
            return new QueryMode(this);
        }

        //region Helpers
        protected void SetTitle(int stringResource){
            if (mListener != null) {
                mListener.onFragmentInteraction(mActivity.getString(stringResource));
            }
        }

        private void clearMapQueryPoints() {
            for(Marker marker : mMarkers){
                marker.remove();
            }

            mMarkers.clear();

            if(mShape != null) {
                mShape.remove();

                mShape = null;
            }
        }
        //endregion

        //region Box Query Steps
        protected void boxQueryStep2AddPoint(double lat, double lng){
            if(mQueryMachine.isState(QueryMachine.State.BOXQUERY_DRAWN)){
                clearMapQueryPoints();
            }
            MarkerOptions options = getMarkerOptions(lat, lng);

            mMarkers.add(mMap.addMarker(options));

            if(mMarkers.size() == BOX){
                boxQueryStep3CompleteBox();
                mQueryMachine.setState(QueryMachine.State.BOXQUERY_DRAWN);
                boxQueryStep4SendToGeoU();
            } else {
                mQueryMachine.setState(QueryMachine.State.BOXQUERY_INPROGRESS);
            }
        }

        protected MarkerOptions getMarkerOptions(double latitude, double longitude) {
            return new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_radius_black_48dp))
                    .anchor(.5f, .5f);
        }

        protected void boxQueryStep3CompleteBox() {
            LatLng first = mMarkers.get(0).getPosition();
            LatLng second = mMarkers.get(1).getPosition();

            MarkerOptions options = getMarkerOptions(first.latitude, second.longitude);
            MarkerOptions options2 = getMarkerOptions(second.latitude, first.longitude);

            mMarkers.add(mMap.addMarker(options));
            mMarkers.add(mMap.addMarker(options2));

            List<Marker> reOrder = new ArrayList<>();
            reOrder.addAll(mMarkers);

            mMarkers.clear();
            mMarkers.add(reOrder.get(0));
            mMarkers.add(reOrder.get(2));
            mMarkers.add(reOrder.get(1));
            mMarkers.add(reOrder.get(3));

            setCameraToBoxExtent();
        }

        protected void setCameraToBoxExtent() {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(Marker m : mMarkers) {
                builder.include(m.getPosition());
            }

            LatLngBounds bounds = builder.build();

            int padding = 80;
            CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(u);
        }

        protected void boxQueryStep4SendToGeoU(){
            PolygonOptions options = new PolygonOptions().fillColor(0x33000000).strokeWidth(2).strokeColor(Color.BLACK);

            for(int i = 0; i < BOX + 2; i++){
                options.add(mMarkers.get(i).getPosition());
            }

            mShape = mMap.addPolygon(options);

            Collections.sort(mMarkers, new MarkerComparer());
            Min min = new Min(mMarkers.get(2));
            Max max = new Max(mMarkers.get(0));

            QuerySenderHelper q = new QuerySenderHelper();
            q.sendBoxQuery(max, min);

            //clearMapQueryPoints();

            //anchorSlider();

        }
        //endregion

        //region Point Query Helpers
        private void setPointMarker(double latitude, double longitude) {
            mQueryMachine.setState(QueryMachine.State.POINTQUERY_DRAWN);

            MarkerOptions options = getMarkerOptions(latitude, longitude);

            clearMapQueryPoints();
            mMarkers.add(mMap.addMarker(options));

            CameraUpdate u = CameraUpdateFactory.newLatLng(options.getPosition());
            mMap.animateCamera(u);

            pointQueryStep2SendToGeoU();
        }

        private void pointQueryStep2SendToGeoU() {

        }
        //endregion

        //region Reset Helpers
        protected void Reset() {
            mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            clearMapQueryPoints();
            SetVisibility(mControls.values(), View.GONE);
            mMap.setOnMapClickListener(null);
            SetTitle(R.string.app_name);
        }

        protected void SetVisibility(Iterable<FloatingActionButton> controls, int visibility) {
            for(FloatingActionButton control : controls){
                control.setVisibility(visibility);
            }
        }

        private void UpdateState() {
            switch (mQueryMachine.getState()){
                case QueryMachine.State.POINTQUERY_DRAWN:
                    mQueryMachine.setState(QueryMachine.State.POINTQUERY_INPROGRESS);
                    break;
                case QueryMachine.State.BOXQUERY_DRAWN:
                case QueryMachine.State.BOXQUERY_INPROGRESS:
                    mQueryMachine.setState(QueryMachine.State.BOXQUERY_STARTED);
                    break;
            }
        }

        protected Boolean ShouldOnlyClear(){
            return mQueryMachine.isState(QueryMachine.State.POINTQUERY_DRAWN)
                    || mQueryMachine.isState(QueryMachine.State.BOXQUERY_DRAWN)
                    || mQueryMachine.isState(QueryMachine.State.BOXQUERY_INPROGRESS);
        }

        private boolean ShouldReset() {
            return mQueryMachine.isState(QueryMachine.State.PICKQUERY)
                    || mQueryMachine.isState(QueryMachine.State.POINTQUERY_INPROGRESS)
                    || mQueryMachine.isState(QueryMachine.State.BOXQUERY_STARTED);
        }
        //endregion

    }
}
