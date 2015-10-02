package com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons;

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
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet.TabletFolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet.TabletPermissionsTab;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class LayerFolderDetailCommons implements ILayerFolderDetailCommons {

    ISlidingPanelManager mPanelManager;
    Folder mEntity;

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";

    @Override
    public ILayerFolderDetailCommons panel(SlidingUpPanelLayout panel) {
        application.setLayerDetailFragmentPanel(panel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_DETAIL).hide().build();

        return this;
    }

    @Override
    public ILayerFolderDetailCommons tabHost(FragmentTabHost tabHost, Resources resources, Bundle args) {
        args.putString("Folder Type", "Document");

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(DETAILS), TabletFolderDetailsTab.class, args);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(PERMISSIONS), TabletPermissionsTab.class, args);

        tabHost.setCurrentTab(0);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) vg.getChildAt(1);
            tv.setTextColor(resources.getColor(R.color.white));
        }

        return this;
    }

    @Override
    public Folder handleArguments(Bundle args) {
        mEntity = args.getParcelable(Folder.FOLDER_INTENT);
        return mEntity;
    }
}
