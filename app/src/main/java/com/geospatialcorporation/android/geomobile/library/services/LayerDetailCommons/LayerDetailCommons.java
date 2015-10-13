package com.geospatialcorporation.android.geomobile.library.services.LayerDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.util.TabHostUtil;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.AttributeLayoutTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.DetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.SublayersTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet.TabletAttributeLayoutTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet.TabletDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet.TabletSublayersTab;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class LayerDetailCommons implements ILayerDetailCommons {

    ISlidingPanelManager mPanelManager;

    private static final String SUBLAYERS = "Sublayers";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DETAILS = "Details";

    @Override
    public Layer handleArguments(Bundle args) {
        return args.getParcelable(Layer.LAYER_INTENT);
    }

    @Override
    public ILayerDetailCommons panel(SlidingUpPanelLayout panel) {
        application.setLayerDetailFragmentPanel(panel);

        mPanelManager = new PanelManager.Builder()
                                .type(GeoPanel.LAYER_DETAIL)
                                .hide()
                                .build();

        return this;
    }

    @Override
    public void tabHost(FragmentTabHost tabHost, Resources resources, Bundle args) {

        Class<?> sublayerTabClass, attributeLayoutClass, detailsTabClass;

        if(application.getIsTablet()){
            sublayerTabClass = TabletSublayersTab.class;
            attributeLayoutClass = TabletAttributeLayoutTab.class;
            detailsTabClass = TabletDetailsTab.class;
        } else {
            sublayerTabClass = SublayersTab.class;
            attributeLayoutClass = AttributeLayoutTab.class;
            detailsTabClass = DetailsTab.class;
        }

        tabHost.addTab(tabHost.newTabSpec(SUBLAYERS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.sublayers_selector)), sublayerTabClass, args);
        tabHost.addTab(tabHost.newTabSpec(ATTRIBUTES).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.attr_selector)), attributeLayoutClass, args);
        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.details_selector)), detailsTabClass, args);

        tabHost.setCurrentTab(0);

    }

    @Override
    public ISlidingPanelManager getPanelManager(SlidingUpPanelLayout panel) {
        if(mPanelManager == null) {
            application.setLayerDetailFragmentPanel(panel);

            mPanelManager = new PanelManager.Builder()
                    .type(GeoPanel.LAYER_DETAIL)
                    .hide()
                    .build();
        }

        return  mPanelManager;
    }
}
