package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureAttributesTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureDocumentsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureMapInfoTab;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureWindowPanelFragment extends GeoViewFragmentBase {

    private static final String MAPINFO = "Map Info";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DOCUMENTS = "Documents";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_panel_featurewindow, container, false);

        ButterKnife.inject(this, view);

        mPanelManager = new PanelManager(GeoPanel.MAP);

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(MAPINFO).setIndicator(MAPINFO), FeatureMapInfoTab.class, getArguments());
        tabHost.addTab(tabHost.newTabSpec(ATTRIBUTES).setIndicator(ATTRIBUTES), FeatureAttributesTab.class, getArguments());
        tabHost.addTab(tabHost.newTabSpec(DOCUMENTS).setIndicator(DOCUMENTS), FeatureDocumentsTab.class, getArguments());

        tabHost.setCurrentTab(0);

        return view;
    }

}
