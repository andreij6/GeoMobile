package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons.ILayerFolderDetailCommons;
import com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons.LayerFolderDetailCommons;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;

public class TabLayerFolderDetailFragment extends TabGeoViewFragmentBase {

    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.tabHost) FragmentTabHost mTabHost;

    ILayerFolderDetailCommons mCommon;
    Folder mFolder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tree_detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommon = new LayerFolderDetailCommons();

        mFolder = mCommon.handleArguments(getArguments());

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mCommon.panel(mPanel)
                .tabHost(mTabHost, getResources(), getArguments());

        return v;
    }
}
