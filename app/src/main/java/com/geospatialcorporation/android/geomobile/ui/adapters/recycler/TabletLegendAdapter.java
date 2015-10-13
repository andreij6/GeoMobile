package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.AppStateSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.SendMapQueryRequestCallback;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.Layers;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.Options;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabLayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabletLayerDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLayerFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TabletLegendAdapter extends GeoRecyclerAdapterBase<TabletLegendAdapter.Holder, LegendLayer> {

    //IFullDataRepository<Layer> mLayerRepo;
    List<Integer> mLayerIdsToShow;
    AppStateSharedPrefs mStateSharedPrefs;
    ILayerManager mLayerManager;
    IMapStatusBarManager mMapStatusBarManager;
    ISlidingPanelManager mPanelManager;
    Subscription mSubscription;

    public TabletLegendAdapter(Context context, List<LegendLayer> layers, ISlidingPanelManager panelManager) {
        super(context, layers, R.layout.recycler_legend_layers, Holder.class);
        //mLayerRepo = new LayersAppSource();
        mStateSharedPrefs = application.getGeoSharedPrefsComponent().provideAppStateSharedPrefs();
        mLayerManager = application.getLayerManager();
        mMapStatusBarManager = application.getStatusBarManager();
        mSubscription = application.getGeoSubscription();
        mPanelManager = panelManager;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }


    public class Holder extends GeoHolderBase<LegendLayer> {

        //region ButterKnife
        @Bind(R.id.layerNameTV)TextView mLayerName;
        @Bind(R.id.isVisible)CheckBox isVisibleCB;
        @Bind(R.id.sublayerExpander)ImageView gotoSublayer;
        @Bind(R.id.geometryIV) ImageView geomIV;
        @Bind(R.id.folderImageView) ImageView mFolderIcon;
        //endregionxxx

        Layer mLayer;
        LegendLayer mLegendLayer;
        Folder mFolder;

        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(LegendLayer llayer) {

            if(llayer.getLayer() != null) {

                mLayer = llayer.getLayer();
                mLegendLayer = llayer;

                isVisibleCB.setVisibility(View.VISIBLE);
                gotoSublayer.setVisibility(View.VISIBLE);
                geomIV.setVisibility(View.VISIBLE);
                mFolderIcon.setVisibility(View.GONE);
                gotoSublayer.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_information_outline_grey600_18dp));

                mLayerName.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
                mLayerName.setTextColor(mContext.getResources().getColor(R.color.primary_text));

                mView.setBackgroundColor(mContext.getResources().getColor(R.color.white));

                if (!mLegendLayer.isIconSet()) {
                    mLegendLayer.setLegendIcon(getDrawable(mLayer.getGeometryTypeCodeId()));
                }

                mLayerName.setText(mLayer.getName());
                mLayerName.setOnClickListener(ZoomToLayer);

                isVisibleCB.setChecked(mLayer.getIsShowing());
                isVisibleCB.setEnabled(true);

                isVisibleCB.setOnClickListener(ToggleShowLayers);
                gotoSublayer.setOnClickListener(GoToSublayer);

                mLegendLayer.setImageView(geomIV);
                mLegendLayer.setImageSrc(mContext);
                mLegendLayer.setCheckBox(isVisibleCB);


            } else {
                mFolder = llayer.getFolder();

                mView.setBackgroundColor(mContext.getResources().getColor(R.color.body_bkg));

                mLayerName.setText(mFolder.getProperName().toUpperCase());

                mLayerName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                mLayerName.setTextColor(mContext.getResources().getColor(R.color.primary_text));
                mLayerName.setTypeface(null, Typeface.BOLD);

                mFolderIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_information_outline_grey600_18dp));
                mFolderIcon.setOnClickListener(ShowLayerFolderDetails);
                mLayerName.setOnClickListener(ShowLayerFolderDetails);

                gotoSublayer.setOnClickListener(ShowLayerFolderOptions);

                isVisibleCB.setVisibility(View.INVISIBLE);
                geomIV.setVisibility(View.INVISIBLE);
                gotoSublayer.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_plus_circle_outline_grey600_18dp));
                gotoSublayer.setBackgroundColor(Color.TRANSPARENT);
                mFolderIcon.setVisibility(View.VISIBLE);
            }

        }

        //region Click Listeners
        protected View.OnClickListener ToggleShowLayers = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLayers(false);
            }
        };

        protected void toggleLayers(Boolean alwaysShowLayers){

            Boolean alreadyShowing = false;

            if(alwaysShowLayers){
                alreadyShowing = isVisibleCB.isChecked();
                isVisibleCB.setChecked(true);
            }

            if(!alreadyShowing) {
                if (isVisibleCB.isChecked()) {
                    //Show Layer
                    if(mLegendLayer.isMapped()){
                        return;
                    }
                    isVisibleCB.setEnabled(false);

                    mAnalytics.trackClick(new GoogleAnalyticEvent().ShowLayer());

                    //set Layer Loading
                    //mMapStatusBarManager.setLayerMessage(mLayer.getName());

                    addLayerToMap();

                    application.getMapLayerState().addLayer(mLayer);

                    updateLayerDB(true);

                    mLayerManager.addVisibleLayerExtent(mLayer.getId(), mLayer.getExtent());

                    mLegendLayer.setMapped(true);
                } else {
                    //remove layer
                    mAnalytics.trackClick(new GoogleAnalyticEvent().HideLayer());

                    mLayerManager.removeLayer(mLayer.getId(), mLayer.getGeometryTypeCodeId());

                    //probably move this the Layer manager removeLayer
                    mLayer.setIsShowing(false);

                    mLegendLayer.setLegendIcon(getDrawable(mLayer.getGeometryTypeCodeId()));
                    mLegendLayer.setImageSrc(mContext);

                    application.getMapLayerState().removeLayer(mLayer);

                    updateLayerDB(false);

                    mLayerManager.removeExtent(mLayer.getId());

                    mLegendLayer.setMapped(false);
                }
            }
        }

        protected void updateLayerDB(Boolean isShowing) {
            String layerKey = mLayer.getName() + mLayer.getId() + "_"+ mSubscription.getId();

            if(isShowing) {
                mStateSharedPrefs.add(layerKey, mLayer.getId());
                mStateSharedPrefs.apply();
            } else {
                mStateSharedPrefs.remove(layerKey);
                mStateSharedPrefs.commit();
            }
        }

        protected View.OnClickListener ShowLayerFolderOptions = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPanelManager.getIsOpen()){

                    Fragment f = new TabletLayerFolderPanelFragment();

                    f.setArguments(mFolder.toBundle());

                    ((MainTabletActivity)mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.slider_content, f)
                            .commit();

                    mPanelManager.halfAnchor();
                    mPanelManager.touch(false);
                } else {
                    mPanelManager.hide();
                }
            }
        };

        protected View.OnClickListener ShowLayerFolderDetails = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment frag = new TabLayerFolderDetailFragment();
                FragmentManager fm = ((MainTabletActivity)mContext).getSupportFragmentManager();

                frag.setArguments(mFolder.toBundle());

                fm.beginTransaction()
                        .replace(R.id.info_frame, frag)
                        .addToBackStack(null)
                        .commit();
            }
        };

        private View.OnClickListener GoToSublayer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new TabletLayerDetailFragment();
                FragmentManager fm = ((MainTabletActivity)mContext).getSupportFragmentManager();

                frag.setArguments(mLayer.toBundle());

                fm.beginTransaction()
                        .replace(R.id.info_frame, frag)
                        .addToBackStack(null)
                        .commit();

            }
        };

        private View.OnClickListener ZoomToLayer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayerManager.zoomToExtent(mLayer.getExtent());

                toggleLayers(true);
            }
        };
        //endregion

        //region Helpers
        protected void addLayerToMap() {
            Layers single = new Layers(mLayer);
            List<Layers> layers = new ArrayList<>();
            layers.add(single);

            MapDefaultQueryRequest request = new MapDefaultQueryRequest(layers, Options.MAP_QUERY);

            ILayerStyleTask layerStyleTask = application.getTasksComponent().provideLayerStyleTask();

            layerStyleTask.getStyle(mLegendLayer, new SendMapQueryRequestCallback(request, mLegendLayer));
        }

        protected Drawable getDrawable(Integer geometryTypeCodeId) {
            Drawable d = ContextCompat.getDrawable(mContext,R.drawable.ic_window_minimize_grey600_18dp);

            switch (geometryTypeCodeId) {
                case GeometryTypeCodes.Line:
                case GeometryTypeCodes.MultiLine:
                    d = ContextCompat.getDrawable(mContext,R.drawable.ic_window_minimize_grey600_18dp);
                    break;
                case GeometryTypeCodes.Point:
                case GeometryTypeCodes.MultiPoint:
                    d = ContextCompat.getDrawable(mContext,R.drawable.ic_checkbox_blank_circle_grey600_18dp);
                    break;
                case GeometryTypeCodes.Polygon:
                case GeometryTypeCodes.MultiPolygon:
                    d = ContextCompat.getDrawable(mContext,R.drawable.ic_hexagon_outline_grey600_18dp);
                    break;
                default:
                    break;
            }

            return d;
        }

        protected MarkerOptions getMarkerOptions(double latitude, double longitude) {
            return new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_circle_black_24dp))
                    .anchor(.5f, .5f);
        }

        protected void goToMap(MainActivity activity) {

            FragmentManager fm = activity.getSupportFragmentManager();

            GoogleMapFragment gFrag = application.getMapFragment();

            fm.beginTransaction()
                    .replace(R.id.content_frame, gFrag)
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(mContext, "Click " + mLayer.getName() + " to Zoom To Extent", Toast.LENGTH_LONG).show();
        }
        //endregion
    }
}
