package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.AttributeLayoutTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.DetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.SublayersTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerDetailPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LayerDetailFragment extends ItemDetailFragment<Layer>
        implements TabHost.OnTabChangeListener
{
    private static final String TAG = LayerDetailFragment.class.getSimpleName();

    private static final String SUBLAYERS = "Sublayers";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DETAILS = "Details";

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable @Bind(R.id.optionsIV) FloatingActionButton mOptions;
    @Nullable @Bind(R.id.parentFolderSplit) ImageView mSplitter;
    @Nullable @Bind(R.id.sectionTitle) TextView mBackFolder;
    @Nullable @Bind(R.id.landOptionsIV) ImageView mLandOptions;

    FragmentTabHost mTabHost;
    View mView;
    IFolderTreeService mFolderService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_tree_detail, null);
        mFolderService = application.getTreeServiceComponent().provideFolderTreeService();

        ButterKnife.bind(this, mView);

        handleArguments();

        application.setLayerDetailFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_DETAIL).hide().build();

        mTabHost = (FragmentTabHost)mView.findViewById(R.id.tabHost);

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec(SUBLAYERS)
                .setIndicator(createTabView(mTabHost.getContext(), R.drawable.sublayers_selector)),
                SublayersTab.class, getArguments());

        mTabHost.addTab(
                mTabHost.newTabSpec(ATTRIBUTES)
                        .setIndicator(createTabView(mTabHost.getContext(), R.drawable.attr_selector)),
                AttributeLayoutTab.class, getArguments());

        mTabHost.addTab(
                mTabHost.newTabSpec(DETAILS)
                        .setIndicator(createTabView(mTabHost.getContext(), R.drawable.details_selector)),
                DetailsTab.class, getArguments());

        mTabHost.setCurrentTab(0);

        if(mOptions != null) {
            mOptions.bringToFront();
        }

        return mView;
    }

    @Override
    protected Fragment getPanelFragment() {
        return new LayerDetailPanelFragment();
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Layer.LAYER_INTENT);

        if(mEntity != null){

            mTitle.setText(DataHelper.trimString(mEntity.getName(), 15));
        }

        Folder folder = mFolderService.getParentFolderByLayerId(mEntity.getId());

        if(folder != null){
            if(!folder.isEditable()) {
                mOptions.setVisibility(View.GONE);
            }
        }
    }

    private void setLandscapeView(Folder parentFolder) {
        if(application.getIsLandscape()){
            mBackFolder.setVisibility(View.VISIBLE);
            mSplitter.setVisibility(View.VISIBLE);

            mBackFolder.setText(parentFolder.getProperName());
        }
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    public Fragment getCurrentTab() {
        return getChildFragmentManager().findFragmentById(android.R.id.tabcontent);
    }


}
