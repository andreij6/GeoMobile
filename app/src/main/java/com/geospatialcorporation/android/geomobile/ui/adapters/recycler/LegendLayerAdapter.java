package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.Layers;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.Options;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class LegendLayerAdapter extends GeoRecyclerAdapterBase<LegendLayerAdapter.Holder, LegendLayer> {

    //IFullDataRepository<Layer> mLayerRepo;
    List<Integer> mLayerIdsToShow;
    AppStateSharedPrefs mStateSharedPrefs;
    ILayerManager mLayerManager;
    ILayerStyleTask mLayerStyleTask;
    IMapStatusBarManager mMapStatusBarManager;

    public LegendLayerAdapter(Context context, List<LegendLayer> layers) {
        super(context, layers, R.layout.recycler_legend_layers, Holder.class);
        //mLayerRepo = new LayersAppSource();
        mStateSharedPrefs = application.getGeoSharedPrefsComponent().provideAppStateSharedPrefs();
        mLayerManager = application.getMapComponent().provideLayerManager();
        mLayerStyleTask = application.getTasksComponent().provideLayerStyleTask();
        mMapStatusBarManager = application.getUIHelperComponent().provideMapStatusBarManager();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    public class Holder extends GeoHolderBase<LegendLayer> {

        //region ButterKnife
        @InjectView(R.id.layerNameTV) TextView mLayerName;
        @InjectView(R.id.isVisible) CheckBox isVisibleCB;
        @InjectView(R.id.sublayerExpander) ImageView gotoSublayer;
        @InjectView(R.id.geometryIV) ImageView geomIV;
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
                gotoSublayer.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
                gotoSublayer.setImageDrawable(mContext.getDrawable(R.drawable.ic_information_outline_white_18dp));

                mLayerName.setTextAppearance(mContext, android.R.style.TextAppearance_DeviceDefault_Medium);
                mLayerName.setTextColor(mContext.getResources().getColor(R.color.white));

                mView.setBackgroundColor(mContext.getResources().getColor(R.color.primary));

                if (!mLegendLayer.isIconSet()) {
                    mLegendLayer.setLegendIcon(getDrawable(mLayer.getGeometryTypeCodeId()));
                }

                mLayerName.setText(mLayer.getName());
                mLayerName.setOnClickListener(ZoomToLayer);

                if(IsSetInAppState()) {
                    mLayer.setIsShowing(true);
                }

                isVisibleCB.setChecked(mLayer.getIsShowing());
                isVisibleCB.setEnabled(true);

                isVisibleCB.setOnClickListener(ToggleShowLayers);
                gotoSublayer.setOnClickListener(GoToSublayer);

                if(mLayer.getIsShowing()  && !mLegendLayer.isMapped()){
                    isVisibleCB.callOnClick();
                }

                mLegendLayer.setImageView(geomIV);
                mLegendLayer.setImageSrc();
                //mLegendLayer.setProgressBar(mProgressBar);
                mLegendLayer.setCheckBox(isVisibleCB);


            } else {
                mFolder = llayer.getFolder();

                mView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));

                mLayerName.setText("> " + mFolder.getPrettyPath());

                mLayerName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                mLayerName.setTextColor(mContext.getResources().getColor(R.color.primary_dark));

                mLayerName.setOnClickListener(GoToLayerFragment);

                isVisibleCB.setVisibility(View.INVISIBLE);
                geomIV.setVisibility(View.GONE);
                //mProgressBar.setVisibility(View.GONE);
                gotoSublayer.setImageDrawable(mContext.getDrawable(R.drawable.ic_information_outline_black_18dp));
                gotoSublayer.setBackgroundColor(Color.TRANSPARENT);
            }

        }

        protected boolean IsSetInAppState() {
            return false;  //TODO: For some reason
            //return mStateSharedPrefs.getInt(mLayer.getName() + mLayer.getId(), 0) != 0;
        }

        //region Click Listeners
        protected View.OnClickListener ToggleShowLayers = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisibleCB.isChecked()) {
                    //Show Layer
                    isVisibleCB.setEnabled(false);

                    mAnalytics.trackClick(new GoogleAnalyticEvent().ShowLayer());

                    //set Layer Loading
                    Fragment contentFrag = application.getMainActivity().getContentFragment();

                    mMapStatusBarManager.setMessage("Loading " +  mLayer.getName());

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
                    mLegendLayer.setImageSrc();

                    application.getMapLayerState().removeLayer(mLayer);

                    updateLayerDB(false);

                    mLayerManager.removeExtent(mLayer.getId());

                    mLegendLayer.setMapped(false);
                }
            }
        };

        protected void updateLayerDB(Boolean isShowing) {
            if(isShowing) {
                mStateSharedPrefs.add(mLayer.getName() + mLayer.getId(), mLayer.getId());
                mStateSharedPrefs.apply();
            } else {
                mStateSharedPrefs.remove(mLayer.getName() + mLayer.getId());
                mStateSharedPrefs.apply();
            }
        }

        protected View.OnClickListener GoToLayerFragment = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment layerFragment = new LayerFragment();

                FragmentManager fragmentManager = application.getMainActivity().getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putInt(Folder.FOLDER_INTENT, mFolder.getId());
                layerFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, layerFragment)
                        .addToBackStack(null).commit();

                closeDrawer((MainActivity)mContext);
            }
        };



        private View.OnClickListener GoToSublayer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mContext;
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

        private View.OnClickListener ZoomToLayer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mContext;

                Fragment currentFragment = activity.getContentFragment();

                if (!(currentFragment instanceof GoogleMapFragment)) {
                    goToMap(activity);
                } else {
                    mLayerManager.zoomToExtent(mLayer.getExtent());
                }

                closeDrawer(activity);
            }
        };


        protected MarkerOptions getMarkerOptions(double latitude, double longitude) {
            return new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_circle_black_24dp))
                    .anchor(.5f, .5f);
        }

        protected void closeDrawer(MainActivity activity) {
            activity.closeLayerDrawer();
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

        //region Helpers
        protected void addLayerToMap() {
            Layers single = new Layers(mLayer);
            List<Layers> layers = new ArrayList<>();
            layers.add(single);

            MapDefaultQueryRequest request = new MapDefaultQueryRequest(layers, Options.MAP_QUERY);

            mLayerStyleTask.getStyle(mLegendLayer, new SendMapQueryRequestCallback(request, mLegendLayer));
        }

        protected Drawable getDrawable(Integer geometryTypeCodeId) {
            Drawable d = mContext.getDrawable(R.drawable.ic_window_minimize_white_18dp);

            switch (geometryTypeCodeId) {
                case GeometryTypeCodes.Line:
                case GeometryTypeCodes.MultiLine:
                    d = mContext.getDrawable(R.drawable.ic_window_minimize_white_18dp);
                    break;
                case GeometryTypeCodes.Point:
                case GeometryTypeCodes.MultiPoint:
                    d = mContext.getDrawable(R.drawable.ic_checkbox_blank_circle_white_18dp);
                    break;
                case GeometryTypeCodes.Polygon:
                case GeometryTypeCodes.MultiPolygon:
                    d = mContext.getDrawable(R.drawable.ic_hexagon_outline_white_18dp);
                    break;
                default:
                    break;
            }

            return d;
        }
        //endregion
    }
}
