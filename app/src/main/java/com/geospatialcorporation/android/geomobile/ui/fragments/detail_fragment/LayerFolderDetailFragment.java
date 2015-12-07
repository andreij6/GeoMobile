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
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerFolderDetailPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LayerFolderDetailFragment extends ItemDetailFragment<Folder> implements TabHost.OnTabChangeListener {

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable @Bind(R.id.optionsIV) FloatingActionButton mOptions;
    @Nullable @Bind(R.id.parentFolderSplit) ImageView mSplitter;
    @Nullable @Bind(R.id.sectionTitle) TextView mBackFolder;
    @Nullable @Bind(R.id.landOptionsIV) ImageView mLandOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_tree_detail, null);

        ButterKnife.bind(this, view);

        application.setLayerDetailFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_DETAIL).hide().build();

        sendScreenName();

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        handleArguments();
        Bundle args = getArguments();
        args.putString(Folder.FOLDER_TYPE_INTENT, "Layer");

        tabHost.addTab(
                tabHost.newTabSpec(DETAILS)
                        .setIndicator(createTabView(tabHost.getContext(), R.drawable.details_selector)), FolderDetailsTab.class, args);

        tabHost.addTab(
                tabHost.newTabSpec(PERMISSIONS)
                        .setIndicator(createTabView(tabHost.getContext(), R.drawable.permissions_selector)), PermissionsTab.class, getArguments());

        tabHost.setCurrentTab(0);

        if(mOptions != null) {
            mOptions.bringToFront();
        }

        return view;
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    protected Fragment getPanelFragment() {
        return new LayerFolderDetailPanelFragment();
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        if(mEntity != null){
            mTitle.setText(DataHelper.trimString(mEntity.getProperName(), 15));
        }
    }

    protected void sendScreenName() {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().LayerFolderDetail());
    }


}
