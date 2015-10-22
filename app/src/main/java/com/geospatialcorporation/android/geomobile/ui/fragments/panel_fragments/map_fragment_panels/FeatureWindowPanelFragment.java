package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.util.TabHostUtil;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureAttributesTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureDocumentsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureMapInfoTab;
import com.google.android.gms.maps.model.Marker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeatureWindowPanelFragment extends GeoViewFragmentBase {
    private static final String TAG = FeatureWindowPanelFragment.class.getSimpleName();

    private static final String MAPINFO = "Map Info";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DOCUMENTS = "Documents";
    protected FeatureQueryResponse mResponse;
    Bundle mArgs;
    GoogleMapFragment mContentFragment;
    @Bind(R.id.layerNameTV) TextView FeatureName;
    @Bind(R.id.nextIV) ImageView mNextIV;
    @Bind(R.id.previousIV) ImageView mPreviousIV;
    Boolean mIsPointFeature;

    @OnClick(R.id.previousIV)
    public void closeFeatureWindow(){
        mContentFragment.getPrevious();
    }

    @OnClick(R.id.nextIV)
    public void nextFeature(){ mContentFragment.getNextFeature(); }

    @OnClick(R.id.rezoomIV)
    public void rezoomToFeature(){ mContentFragment.rezoomToHighlight(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_panel_featurewindow, container, false);

        ButterKnife.bind(this, view);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.touch(true);

        mContentFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

        handleArgs();

        //if(!mIsPointFeature){
        //    mNextIV.setVisibility(View.GONE);
        //    mPreviousIV.setVisibility(View.GONE);
        //}

        FeatureName.setText(getFeatureName());

        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(MAPINFO).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.details_selector)), FeatureMapInfoTab.class, mArgs);
        tabHost.addTab(tabHost.newTabSpec(ATTRIBUTES).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.attr_selector)), FeatureAttributesTab.class, mArgs);
        tabHost.addTab(tabHost.newTabSpec(DOCUMENTS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.documents_selector)), FeatureDocumentsTab.class, mArgs);

        tabHost.setCurrentTab(mContentFragment.getFeatureWindowTab());

        mContentFragment.setFeatureWindowTab(0);

        return view;
    }

    protected String getFeatureName() {
        String result = "Feature Info";
        try{
            String firstPropertyValue = mResponse.getFeatures().get(0).getAttributes().get(0);

            if(firstPropertyValue != null && !firstPropertyValue.isEmpty()) {
                if(firstPropertyValue.length() < 16) {
                    result = firstPropertyValue + " | Feature Info";
                } else {
                    result = firstPropertyValue;
                }
            }
        } catch (Exception e){
            Log.d(TAG, e.getMessage());

            mAnalytics.sendException(e);
        }

        return result;
    }

    protected void handleArgs() {
        mArgs = getArguments();

        ParcelableFeatureQueryResponse data = mArgs.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        if (data != null && data.getFeatureQueryResponse().size() > 0) {
            mResponse = data != null ? data.getFeatureQueryResponse().get(0) : null;
        }

    }


    public Fragment initialize(Marker highlightedMarker) {
        if(highlightedMarker != null){
            mIsPointFeature = true;
        } else {
            mIsPointFeature = false;
        }

        return this;
    }
}
