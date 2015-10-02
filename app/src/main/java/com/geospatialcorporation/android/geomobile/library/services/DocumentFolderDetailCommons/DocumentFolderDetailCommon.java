package com.geospatialcorporation.android.geomobile.library.services.DocumentFolderDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet.TabletFolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet.TabletPermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletDocumentFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class DocumentFolderDetailCommon implements IDocumentFolderDetailCommon {

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";
    ISlidingPanelManager mPanelManager;

    @Override
    public Folder handleArguments(Bundle args) {
        return args.getParcelable(Folder.FOLDER_INTENT);
    }

    @Override
    public IDocumentFolderDetailCommon panel(SlidingUpPanelLayout panel) {
        application.setDocumentFolderFragmentPanel(panel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.DOCUMENT_FOLDER_DETAIL).hide().build();

        return this;
    }

    @Override
    public void tabHost(FragmentTabHost tabHost, Resources resources, Bundle args) {
        args.putString("Folder Type", "Document");

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(DETAILS), TabletFolderDetailsTab.class, args);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(PERMISSIONS), TabletPermissionsTab.class, args);

        tabHost.setCurrentTab(0);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) vg.getChildAt(1);
            tv.setTextColor(resources.getColor(R.color.white));
        }
    }

    @Override
    public void onOptionsButtonPressed(Folder folder, FragmentManager fragmentManager, TabletDocumentFolderPanelFragment tabletDocumentFolderPanelFragment) {
        if(mPanelManager.getIsOpen()){
            mPanelManager.hide();
        } else {
            Fragment f = new TabletDocumentFolderPanelFragment();

            f.setArguments(folder.toBundle());

            fragmentManager.beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        }
    }

    @Override
    public void closePanel() {
        mPanelManager.hide();
    }
}
