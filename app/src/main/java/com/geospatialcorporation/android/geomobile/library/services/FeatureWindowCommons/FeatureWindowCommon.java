package com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.util.TabHostUtil;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureAttributesTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureDocumentsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureMapInfoTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.tablet.*;

public class FeatureWindowCommon implements IFeatureWindowCommon {

    private static final String TAG = FeatureWindowCommon.class.getSimpleName();

    private static final String MAPINFO = "Map Info";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DOCUMENTS = "Documents";

    @Override
    public FeatureQueryResponse handleArguments(Bundle args) {
        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        assert data != null;

        return data.getFeatureQueryResponse().get(0);
    }

    @Override
    public void tabHost(FragmentTabHost tabHost, Resources resources, Bundle arguments, int currentTab) {

        Class<?> featureMapInfo, featureAttributes, featureDocuments;

        if(application.getIsTablet()){
            featureMapInfo = TabletFeatureMapInfoTab.class;
            featureAttributes = TabletFeatureAttributesTab.class;
            featureDocuments = TabletFeatureDocumentsTab.class;
        } else {
            featureMapInfo = FeatureMapInfoTab.class;
            featureAttributes = FeatureAttributesTab.class;
            featureDocuments = FeatureDocumentsTab.class;
        }

        tabHost.addTab(tabHost.newTabSpec(MAPINFO).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.details_selector)), featureMapInfo, arguments);
        tabHost.addTab(tabHost.newTabSpec(ATTRIBUTES).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.attr_selector)), featureAttributes, arguments);
        tabHost.addTab(tabHost.newTabSpec(DOCUMENTS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.documents_selector)), featureDocuments, arguments);

        tabHost.setCurrentTab(currentTab);

        application.setCurrentFeatureWindowTab(0);

    }

    @Override
    public void setFeatureName(FeatureQueryResponse response, TextView featureName) {
        String result = "Feature Info";
        try{
            String firstPropertyValue = response.getFeatures().get(0).getAttributes().get(0);

            if(firstPropertyValue != null && !firstPropertyValue.isEmpty()) {
                if(firstPropertyValue.length() < 16) {
                    result = firstPropertyValue + " | Feature Info";
                } else {
                    result = firstPropertyValue;
                }
            }
        } catch (Exception e){

        } finally {
            featureName.setText(result);
        }
    }
}
