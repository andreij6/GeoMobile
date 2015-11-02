package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersByFolderTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LayerTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class LayerFragment extends GeoViewFragmentBase implements IContentRefresher, IPostExecuter<Folder> {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    private Context mContext;
    Folder mCurrentFolder;
    DataHelper mDataHelper;
    ProgressDialogHelper mProgressDialogHelper;

    @Bind(R.id.layer_recyclerView) RecyclerView mRecycler;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.layerOptionsIV) ImageView mOptionsSlider;
    @Bind(R.id.showNavIV1) ImageView mNavBars;
    @Bind(R.id.showNavIV2) ImageView mNavLogo;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.backFolder) TextView mBack;


    @OnClick(R.id.showNavIV1)
    public void showNavigation(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.showNavIV2)
    public void showNavigation2(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @OnClick(R.id.layerOptionsIV)
    public void optionsDropDown(){

        if(!mPanelManager.getIsOpen()){

            Fragment f = new LayerFolderPanelFragment();

            f.setArguments(mCurrentFolder.toBundle());

            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        } else {
            closePanel();
        }
    }

    @OnClick(R.id.folderInformation)
    public void folderInfo(){
        Fragment f = new LayerFolderDetailFragment();

        f.setArguments(mCurrentFolder.toBundle());

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, f)
                .commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(inflater, container, R.layout.fragment_layeritems);
        mContext = getActivity();

        mNavLogo.setVisibility(View.GONE);
        mNavBars.setVisibility(View.GONE);
        mTitle.setVisibility(View.INVISIBLE);

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        application.setLayerFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_FRAGMENT).hide().build();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.white));

        handleArguments();

        return mView;
    }

    public void refresh() {
        handleArguments();
    }

    public void handleArguments() {
        Bundle args = getArguments();
        IGetLayersTask task = application.getTasksComponent().provideLayersTask();
        int folderId = 0;

        if(args != null) {
            folderId = args.getInt(Folder.FOLDER_INTENT, 0);
        }

        mProgressDialogHelper = new ProgressDialogHelper(mContext);
        mProgressDialogHelper.toggleProgressDialog();

        task.getByFolder(new GetLayersByFolderTaskParams(getFragmentManager(), mContext, this), folderId);
    }

    @Override
    public void onPostExecute(Folder currentFolder) {
        mCurrentFolder = currentFolder;

        mPanelManager.hide();

        if(currentFolder == null) { return; }

        if (currentFolder.getParent() != null) {
            mNavLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_chevron_left_grey600_18dp));
            mNavLogo.setPadding(-12, 0, 0, 0);

            mNavBars.setOnClickListener(navigateUpTree);
            mNavLogo.setOnClickListener(navigateUpTree);
            mBack.setOnClickListener(navigateUpTree);

            mBack.setVisibility(View.VISIBLE);

        } else {
            mNavBars.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_nav_orange));

            mNavBars.setOnClickListener(showNavigation);
            mNavLogo.setOnClickListener(showNavigation);

            mNavLogo.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_g_logo));
            mNavBars.setVisibility(View.VISIBLE);
        }

        mNavLogo.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(mCurrentFolder.getProperName());

        mDataHelper = new DataHelper();

        List<ListItem> data = mDataHelper.CombineLayerItems(currentFolder.getLayers(), currentFolder.getFolders(), currentFolder.getParent());

        mProgressDialogHelper.hideProgressDialog();

        new LayerTreeSectionBuilder(mContext, getFragmentManager(), currentFolder.getParent(), mPanelManager)
                .BuildAdapter(data, currentFolder.getFolders().size())
                .setRecycler(mRecycler);
    }

    protected View.OnClickListener showNavigation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPanelManager.getIsOpen()){
                closePanel();
            } else {
                ((MainActivity) getActivity()).openNavigationDrawer();
            }
        }
    };

    protected View.OnClickListener navigateUpTree = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPanelManager.getIsOpen()){
                closePanel();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    };

    public void closePanel() {
        mPanelManager.hide();
    }
}