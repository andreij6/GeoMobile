package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LegendLayerSectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.TabletLegendSectionBuilder;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LibraryFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLibraryFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TabLayerFragment extends TabGeoViewFragmentBase
        implements IPostExecuter<List<Folder>>, IContentRefresher, IPanelFragmentCtrl {

    private static final String TAG = TabLayerFragment.class.getSimpleName();

    IGetLayersTask mGetLayersTask;
    ISlidingPanelManager mPanelManager;
    ProgressDialogHelper mProgressDialogHelper;

    @Bind(R.id.layerRecyclerView) RecyclerView mRecycler;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @SuppressWarnings("unused")
    @OnClick(R.id.close)
    public void close(){
        ((MainTabletActivity)getActivity()).closeInfoFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        panel();
        swipeRefresh();
        refresh();

        return v;
    }

    protected void panel() {
        application.setLayerFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_FRAGMENT).hide().build();
    }

    protected void swipeRefresh() {
        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();
        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.accent));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layer_tab_fragment;
    }

    @Override
    public void onPostExecute(List<Folder> folders) {
        mProgressDialogHelper.hideProgressDialog();

        application.setLayerFolders(folders);

        new TabletLegendSectionBuilder(mPanelManager, getActivity())
                .BuildAdapter(folders, 0)
                .setRecycler(mRecycler);
    }

    //region Helpers
    protected IGetLayersTask setTask() {
        if(mGetLayersTask == null){
            return application.getTasksComponent().provideLayersTask();
        }

        return mGetLayersTask;
    }

    @Override
    public void refresh() {
        mProgressDialogHelper = new ProgressDialogHelper(getActivity());
        mProgressDialogHelper.showProgressDialog();

        mGetLayersTask = setTask();

        if(application.areLayerFoldersStored()) {
            onPostExecute(application.getLayerFolders());
        } else {
            mGetLayersTask.getAll(new GetLayersTaskParams(this));
        }
    }

    @Override
    public void closePanel() {
        mPanelManager.hide();
    }
    //endregion
}
