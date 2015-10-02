package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.services.LayerDetailCommons.ILayerDetailCommons;
import com.geospatialcorporation.android.geomobile.library.services.LayerDetailCommons.LayerDetailCommons;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerDetailPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLayerDetailPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.OnClick;

public class TabletLayerDetailFragment extends TabGeoViewFragmentBase implements IPanelFragmentCtrl {

    private static final String TAG = TabletLayerDetailFragment.class.getSimpleName();

    ILayerDetailCommons mCommons;
    ISlidingPanelManager mPanelManager;
    Layer mLayer;

    @Bind(R.id.tabHost) FragmentTabHost mTabHost;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;

    @OnClick(R.id.optionsIV)
    public void bringUpOptions(){
        if(mPanelManager == null){
            mPanelManager = mCommons.getPanelManager(mPanel);
        }

        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment f = new TabletLayerDetailPanelFragment();

            f.setArguments(mLayer.toBundle());

            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommons = new LayerDetailCommons();

        mLayer = mCommons.handleArguments(getArguments());

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mCommons.panel(mPanel).tabHost(mTabHost, getResources(), getArguments());

        return v;
    }

    @Override
    public void closePanel() {
        mPanelManager.hide();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tree_detail;
    }
}
