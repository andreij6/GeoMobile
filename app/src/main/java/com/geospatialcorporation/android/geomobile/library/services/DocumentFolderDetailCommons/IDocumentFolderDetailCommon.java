package com.geospatialcorporation.android.geomobile.library.services.DocumentFolderDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletDocumentFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public interface IDocumentFolderDetailCommon {
    Folder handleArguments(Bundle args);

    IDocumentFolderDetailCommon panel(SlidingUpPanelLayout panel);

    void tabHost(FragmentTabHost tabHost, Resources resources, Bundle args);

    void onOptionsButtonPressed(Folder folder, FragmentManager fragmentManager, TabletDocumentFolderPanelFragment tabletDocumentFolderPanelFragment);

    void closePanel();
}
