package com.geospatialcorporation.android.geomobile.library.services.DocumentDetailCommons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.library.services.DocumentFolderDetailCommons.IDocumentFolderDetailCommon;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLibraryFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public interface IDocumentDetailCommon {
    IDocumentDetailCommon panel(SlidingUpPanelLayout panel);

    IDocumentDetailCommon handleArguments(Bundle args);

    void setViews(TextView documentName, TextView uploadValue, TextView fileSizeValue);

    void closePanel();

    void onOptionsButtonPressed(FragmentManager fragmentManager, Fragment tabletLibraryFolderPanelFragment);
}
