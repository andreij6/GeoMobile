package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons.FeatureWindowCommon;
import com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons.IFeatureWindowCommon;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFeatureWindowCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelStateReactor;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.google.android.gms.maps.model.Marker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeatureWindowPanelFragment extends GeoViewFragmentBase implements IPanelStateReactor {
    private static final String TAG = FeatureWindowPanelFragment.class.getSimpleName();

    protected FeatureQueryResponse mResponse;
    int mLayerId;
    IFeatureWindowCtrl mMapFragment; //TODO: extract interface
    IFeatureWindowCommon mCommon;

    @Bind(R.id.layerNameTV) TextView FeatureName;
    @Bind(R.id.tabHost) FragmentTabHost mTabHost;
    Boolean mIsPointFeature;

    /*@OnClick(R.id.previousIV)
    public void previousFeature(){
        mContentFragment.getPrevious();
    }

    @OnClick(R.id.nextIV)
    public void nextFeature(){ mContentFragment.getNextFeature(); }
    */

    @OnClick(R.id.rezoomIV)
    public void rezoomToFeature(){ mMapFragment.rezoomToHighlight(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommon = new FeatureWindowCommon();
        mMapFragment = application.getFeatureWindowCtrl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_panel_featurewindow, container, false);
        ButterKnife.bind(this, view);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.touch(true);

        mResponse = mCommon.handleArguments(getArguments());
        mCommon.setFeatureName(mResponse, FeatureName);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mCommon.tabHost(mTabHost, getResources(), getArguments(), application.getCurrentFeatureWindowTab(), mLayerId);

        return view;
    }


    public Fragment initialize(Marker highlightedMarker, int selectedLayerId) {
        if(highlightedMarker != null){
            mIsPointFeature = true;
        } else {
            mIsPointFeature = false;
        }

        mLayerId = selectedLayerId;

        return this;
    }

    @Override
    public void Expanded() {
        IPanelStateReactor tab = (IPanelStateReactor)getChildFragmentManager().findFragmentById(android.R.id.tabcontent);

        tab.Expanded();
    }

    @Override
    public void Anchored() {
        IPanelStateReactor tab = (IPanelStateReactor)getChildFragmentManager().findFragmentById(android.R.id.tabcontent);

        tab.Anchored();
    }
}
