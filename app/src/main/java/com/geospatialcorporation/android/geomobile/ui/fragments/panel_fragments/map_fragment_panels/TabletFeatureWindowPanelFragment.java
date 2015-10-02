package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons.FeatureWindowCommon;
import com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons.IFeatureWindowCommon;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import butterknife.Bind;
import butterknife.OnClick;

public class TabletFeatureWindowPanelFragment extends TabGeoViewFragmentBase {

    @Bind(R.id.tabHost) FragmentTabHost mTabHost;
    @Bind(R.id.layerNameTV) TextView FeatureName;

    IFeatureWindowCommon mCommon;
    FeatureQueryResponse mResponse;
    QueryRestService mQueryService;

    @OnClick(R.id.close)
    public void closeFeatureWindow(){
        ((MainTabletActivity)getActivity()).closeInfoFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_panel_featurewindow;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommon = new FeatureWindowCommon();
        mQueryService = new QueryRestService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mResponse = mCommon.handleArguments(getArguments());

        mCommon.setFeatureName(mResponse, FeatureName);

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mCommon.tabHost(mTabHost, getResources(), getArguments(), application.getCurrentFeatureWindowTab());

        return v;
    }

    public void refreshFeatureWindow(int tab) {

        application.setCurrentFeatureWindowTab(tab);

        String featureId = application.getFeatureWindowFeatureId();
        int layerId = application.getFeatureWindowLayerId();

        mQueryService.featureWindow(featureId, layerId);
    }

}
