package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons.ILayerFolderDetailCommons;
import com.geospatialcorporation.android.geomobile.library.services.LayerFolderDetailCommons.LayerFolderDetailCommons;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.OnClick;

public class TabLayerFolderDetailFragment extends TabGeoViewFragmentBase implements IPanelFragmentCtrl {

    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.tabHost) FragmentTabHost mTabHost;
    @Bind(R.id.title) TextView mTitle;

    ILayerFolderDetailCommons mCommon;
    Folder mFolder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tree_detail_tablet;
    }

    @OnClick(R.id.optionsIV)
    public void options(){
        mCommon.onOptionsButtonPressed(mFolder, getFragmentManager(), new TabletFolderPanelFragment());
    }

    @OnClick(R.id.close)
    public void close(){
        ((MainTabletActivity)getActivity()).closeInfoFragment();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommon = new LayerFolderDetailCommons();

        mFolder = mCommon.handleArguments(getArguments());

        mTitle.setText(mFolder.getProperName());

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mCommon.panel(mPanel)
                .tabHost(mTabHost, getResources(), getArguments());

        return v;
    }

    @Override
    public void closePanel() {
        mCommon.closePanel();
    }
}
