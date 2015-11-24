package com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.FragmentTabHost;
import com.geospatialcorporation.android.geomobile.library.constants.PluginCodes;
import com.geospatialcorporation.android.geomobile.library.util.TabHostUtil;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureAttributesTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureDocumentsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureMapInfoTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.VideoDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.VideoPlayTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.tablet.*;
import com.google.gson.Gson;

public class FeatureWindowCommon implements IFeatureWindowCommon {

    private static final String TAG = FeatureWindowCommon.class.getSimpleName();

    private static final String MAPINFO = "Map Info";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DOCUMENTS = "Documents";
    private static final String VIDEO_DETAILS = "Details";
    private static final String VIDEO = "Video";

    FeatureQueryResponse mResponse;

    @Override
    public FeatureQueryResponse handleArguments(Bundle args) {
        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        assert data != null;

        mResponse = data.getFeatureQueryResponse().get(0);

        return mResponse;
    }

    @Override
    public void tabHost(FragmentTabHost tabHost, Resources resources, Bundle arguments, int currentFeatureWindowTab, int layerId) {

        Class<?> featureMapInfo, featureAttributes, featureDocuments;

        if(application.getLayerHashMap().containsKey(layerId)){
            Layer layer = application.getLayerHashMap().get(layerId);

            assert layer != null;

            if(layer.getPluginId() == PluginCodes.Default) {
                if (application.getIsTablet()) {
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

            }

            if(layer.getPluginId() == PluginCodes.VideoLayers){
                Log.d(TAG, "is video layer");


                Class<?> videoDetails, videoPlay;

                if (application.getIsTablet()) {
                    featureMapInfo = TabletFeatureMapInfoTab.class;
                    featureDocuments = TabletFeatureDocumentsTab.class;
                    videoDetails = null;//TabletVideoDetailsTab.class;
                    videoPlay = null;//TabletVideoPlayTab.class;
                } else {
                    featureMapInfo = FeatureMapInfoTab.class;
                    featureDocuments = FeatureDocumentsTab.class;
                    videoDetails = VideoDetailsTab.class;
                    videoPlay = VideoPlayTab.class;
                }

                tabHost.addTab(tabHost.newTabSpec(MAPINFO).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.details_selector)), featureMapInfo, arguments);
                tabHost.addTab(tabHost.newTabSpec(DOCUMENTS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.documents_selector)), featureDocuments, arguments);
                tabHost.addTab(tabHost.newTabSpec(VIDEO).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.video_selector)), videoPlay, arguments);
                tabHost.addTab(tabHost.newTabSpec(VIDEO_DETAILS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.video_details_selector)), videoDetails, arguments);

            }


            tabHost.setCurrentTab(currentFeatureWindowTab);

            application.setCurrentFeatureWindowTab(currentFeatureWindowTab);

        }



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
