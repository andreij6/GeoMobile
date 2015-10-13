package com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.util.TabHostUtil;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet.TabletFolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet.TabletPermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLayerFolderPanelFragment;
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

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.details_selector)), TabletFolderDetailsTab.class, args);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(TabHostUtil.createTabView(tabHost.getContext(), R.drawable.permissions_selector)), TabletPermissionsTab.class, args);

        tabHost.setCurrentTab(0);

        return this;
    }

    @Override
    public Folder handleArguments(Bundle args) {
        mEntity = args.getParcelable(Folder.FOLDER_INTENT);
        return mEntity;
    }

    @Override
    public void closePanel() {
        mPanelManager.hide();
    }

    @Override
    public void onOptionsButtonPressed(Folder folder, FragmentManager fragmentManager, TabletFolderPanelFragment fragment) {
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        } else {
            fragment.setArguments(folder.toBundle());

            fragmentManager.beginTransaction()
                    .replace(R.id.slider_content, fragment)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        }
    }
}
