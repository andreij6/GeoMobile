package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LayerFolderDetailFragment extends ItemDetailFragment<Folder> implements TabHost.OnTabChangeListener {

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";

    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;


    @OnClick(R.id.goBackIV)
    public void goBack(){
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_tree_detail, null);

        ButterKnife.inject(this, view);

        application.setLayerDetailFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_DETAIL).hide().build();

        sendScreenName();

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle args = getArguments();
        args.putString("Folder Type", "Layer");

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(DETAILS), FolderDetailsTab.class, args);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(PERMISSIONS), PermissionsTab.class, getArguments());

        tabHost.setCurrentTab(0);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            ViewGroup vg = (ViewGroup) tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) vg.getChildAt(1);
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        return view;
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        SetTitle(mEntity != null ? mEntity.getName() : null);
    }

    protected void sendScreenName() {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().LayerFolderDetail());
    }
}
