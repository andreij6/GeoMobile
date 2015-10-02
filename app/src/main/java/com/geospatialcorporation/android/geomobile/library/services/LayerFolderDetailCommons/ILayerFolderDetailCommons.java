package com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public interface ILayerFolderDetailCommons {

    ILayerFolderDetailCommons panel(SlidingUpPanelLayout panel);


    ILayerFolderDetailCommons tabHost(FragmentTabHost tabHost, Resources resources, Bundle arguments);

    Folder handleArguments(Bundle arguments);
}
