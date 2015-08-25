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
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureAttributesTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureDocumentsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureMapInfoTab;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureWindowPanelFragment extends GeoViewFragmentBase {
    private static final String TAG = FeatureWindowPanelFragment.class.getSimpleName();

    private static final String MAPINFO = "Map Info";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DOCUMENTS = "Documents";
    protected FeatureQueryResponse mResponse;
    GoogleMapFragment mContentFragment;
    @InjectView(R.id.layerNameTV) TextView FeatureName;

    @OnClick(R.id.close)
    public void closeFeatureWindow(){
        mPanelManager.collapse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_panel_featurewindow, container, false);

        ButterKnife.inject(this, view);

        mPanelManager = new PanelManager(GeoPanel.MAP);
        mPanelManager.touch(false);

        mContentFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

        handleArgs();

        FeatureName.setText(getFeatureName());

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(MAPINFO).setIndicator(MAPINFO), FeatureMapInfoTab.class, getArguments());
        tabHost.addTab(tabHost.newTabSpec(ATTRIBUTES).setIndicator(ATTRIBUTES), FeatureAttributesTab.class, getArguments());
        tabHost.addTab(tabHost.newTabSpec(DOCUMENTS).setIndicator(DOCUMENTS), FeatureDocumentsTab.class, getArguments());

        tabHost.setCurrentTab(mContentFragment.getFeatureWindowTab());

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) vg.getChildAt(1);
            tv.setTextColor(getResources().getColor(R.color.white));
        }

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
        }

        return result;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        mResponse = data != null ? data.getFeatureQueryResponse().get(0) : null; //should only be one for a feature window
    }


}
