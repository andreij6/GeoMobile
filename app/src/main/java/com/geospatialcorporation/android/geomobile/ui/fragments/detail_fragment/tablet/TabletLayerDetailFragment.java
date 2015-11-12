package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.services.LayerDetailCommons.ILayerDetailCommons;
import com.geospatialcorporation.android.geomobile.library.services.LayerDetailCommons.LayerDetailCommons;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
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
    IFolderTreeService mFolderService;
    Layer mLayer;

    @Bind(R.id.tabHost) FragmentTabHost mTabHost;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.optionsIV) ImageView mOptions;

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

            mPanelManager.halfAnchor(0.1f);
            mPanelManager.touch(false);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.close)
    public void close(){
        ((MainTabletActivity)getActivity()).closeInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mFolderService = application.getTreeServiceComponent().provideFolderTreeService();

        mCommons = new LayerDetailCommons();

        mLayer = mCommons.handleArguments(getArguments());

        Folder folder = mFolderService.getParentFolderByLayerId(mLayer.getId());

        if(folder != null && folder.isEditable()){
            mOptions.setVisibility(View.VISIBLE);
        }

        mTitle.setText(mLayer.getName());

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
        return R.layout.fragment_tree_detail_tablet;
    }
}
