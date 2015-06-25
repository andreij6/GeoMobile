package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.AnalyticsHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.map.MapActions;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.Layers;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by andre on 6/5/2015.
 */
public class LegendLayerAdapter extends GeoRecyclerAdapterBase<LegendLayerAdapter.Holder, Layer> {

    QueryRestService mService;

    public LegendLayerAdapter(Context context, List<Layer> layers){
        super(context, layers, R.layout.recycler_legend_layers, Holder.class);
        mService = new QueryRestService();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    public class Holder extends GeoHolderBase<Layer> {

        @InjectView(R.id.layerNameTV) TextView mLayerName;
        @InjectView(R.id.isVisible)CheckBox isVisibleCB;
        @InjectView(R.id.sublayerExpander) ImageView gotoSublayer;

        private Layer mLayer;
        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(Layer layer){
            mLayer = layer;

            mLayerName.setText(layer.getName());
            mLayerName.setOnClickListener(ZoomToLayer);
            isVisibleCB.setChecked(layer.getIsShowing());
            isVisibleCB.setOnClickListener(ToggleShowLayers);
            gotoSublayer.setOnClickListener(GoToSublayer);
        }

        //region Click Listeners
        protected View.OnClickListener ToggleShowLayers = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisibleCB.isChecked()){
                    //Show Layer
                    new AnalyticsHelper().sendClickEvent(R.string.showLayerCheckBox);

                    Layers single = new Layers(mLayer);
                    List<Layers> layers = new ArrayList<>();
                    layers.add(single);

                    MapDefaultQueryRequest request = new MapDefaultQueryRequest(layers);

                    mService.mapQuery(request, mLayer);


                } else {
                    //remove layer
                    new AnalyticsHelper().sendClickEvent(R.string.hideLayerCheckBox);

                    mLayer.clearMapFeatures();
                }
            }
        };

        private View.OnClickListener GoToSublayer = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity activity = (MainActivity)mContext;
                Fragment frag = new LayerDetailFragment();
                FragmentManager fm = activity.getSupportFragmentManager();

                frag.setArguments(mLayer.toBundle());

                fm.beginTransaction()
                        .replace(R.id.content_frame, frag)
                        .addToBackStack(null)
                        .commit();

                closeDrawer(activity);
            }
        };

        private View.OnClickListener ZoomToLayer = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity activity = (MainActivity)mContext;

                Fragment currentFragment = activity.getContentFragment();

                if(!(currentFragment instanceof GoogleMapFragment)){
                    goToMap(activity);
                } else {
                    zoomToLayer();
                }

                closeDrawer(activity);
            }
        };

        protected void zoomToLayer(){
            if(mLayer.getExtent() != null) {
                LatLng first = mLayer.getExtent().getMaxLatLng();
                LatLng second = mLayer.getExtent().getMinLatLng();

                GoogleMap mMap = application.getGoogleMap();

                LatLngBounds bounds = new LatLngBounds(second, first);

                int padding = 50;
                CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(u);
            }
        }

        protected MarkerOptions getMarkerOptions(double latitude, double longitude) {
            return new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_circle_black_24dp))
                    .anchor(.5f, .5f);
        }

        protected void closeDrawer(MainActivity activity) {
            DrawerLayout mDrawerLayout = activity.getRightDrawer();
            View layerView = activity.getLayerListView();

            mDrawerLayout.closeDrawer(layerView);
        }

        protected void goToMap(MainActivity activity) {

            FragmentManager fm = activity.getSupportFragmentManager();

            GoogleMapFragment gFrag = new GoogleMapFragment();

            gFrag.setArguments(mLayer.toBundle());

            fm.beginTransaction()
                    .replace(R.id.content_frame, gFrag)
                    .addToBackStack(null)
                    .commit();
        }
        //endregion
    }
}
