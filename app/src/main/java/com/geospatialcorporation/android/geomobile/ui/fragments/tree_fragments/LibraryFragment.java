package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LibraryTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LibraryFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class LibraryFragment extends GeoViewFragmentBase
        implements IContentRefresher, IPostExecuter<Folder> {
    protected static final String TAG = LibraryFragment.class.getSimpleName();

    //region Properties
    private Folder mCurrentFolder;
    private Context mContext;
    Boolean mIsPanelOpen;
    IGetDocumentsTask mDocumentsTask;
    IDocumentTreeService mUploader;
    DataHelper mHelper;
    IGeneralDialog mDialog;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @InjectView(R.id.libraryOptionsIV) ImageView mOptionsSlider;
    @InjectView(R.id.showNavIV1) ImageView mNavBars;
    @InjectView(R.id.showNavIV2) ImageView mNavLogo;
    @InjectView(R.id.title) TextView mTitle;

    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @OnClick(R.id.libraryOptionsIV)
    public void optionsDropDown(){
        if(!mIsPanelOpen){
            Fragment f = new LibraryFolderPanelFragment();

            f.setArguments(mCurrentFolder.toBundle());

            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);

            mIsPanelOpen = true;

            mOptionsSlider.setImageDrawable(getActivity().getDrawable(R.drawable.ic_close_circle_white_36dp));
        } else {
            mPanelManager.hide();
            mIsPanelOpen = false;
            mOptionsSlider.setImageDrawable(getActivity().getDrawable(R.drawable.ic_dropdown));
        }
    }

    @Override
    public void onCreate(Bundle savedInstance){
        setHasOptionsMenu(false);
        super.onCreate(savedInstance);
    }

    protected void sendScreenName() {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().LibraryTreeScreen());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView(inflater, container, R.layout.fragment_libraryitems);
        mContext = getActivity();

        mNavLogo.setVisibility(View.GONE);
        mNavBars.setVisibility(View.GONE);
        mTitle.setVisibility(View.INVISIBLE);

        sendScreenName();

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        application.setLibraryFragmentPanel(mPanel);

        mPanelManager = new PanelManager(GeoPanel.LIBRARY_FRAGMENT);
        mPanelManager.setup();
        mPanelManager.hide();

        mIsPanelOpen = false;

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        mDocumentsTask = application.getTasksComponent().provideGetDocumentsTask();
        handleArguments();

        mNavigationHelper.syncMenu(2);

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.document_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == Activity.RESULT_OK) {

            mUploader = application.getTreeServiceComponent().provideDocumentTreeService();

            if (requestCode == MainActivity.MediaConstants.PICK_FILE_REQUEST) {
                // Make sure the request was successful

                mUploader.sendDocument(mCurrentFolder, data.getData());

            }

            if(requestCode == MainActivity.MediaConstants.PICK_IMAGE_REQUEST){

                mUploader.sendPickedImage(mCurrentFolder, data.getData());

            }

            if(requestCode == MainActivity.MediaConstants.TAKE_IMAGE_REQUEST) {

                mUploader.sendTakenImage(mCurrentFolder, application.mMediaUri);

            }
        }


    }

    public void refresh() {
        handleArguments();
    }

    protected void handleArguments() {
        Bundle args = getArguments();

        GetDocumentsParam params = new GetDocumentsParam(getFragmentManager(), mCurrentFolder, mRecyclerView, mContext, this);

        if(args != null) {
            int folderId = args.getInt(Folder.FOLDER_INTENT, 0);

            mDocumentsTask.getDocumentsByFolderId(params, folderId);
        } else {
            mDocumentsTask.getDocumentsByFolderId(params, 0);
        }

    }

    @Override
    public void onPostExecute(Folder model) {
        mCurrentFolder = model;

        if(mCurrentFolder.getParent() != null){
            mNavLogo.setImageDrawable(mContext.getDrawable(R.drawable.ic_chevron_left_white_18dp));
            mNavLogo.setPadding(-12, 0, 0, 0);

            mNavBars.setOnClickListener(navigateUpTree);
            mNavLogo.setOnClickListener(navigateUpTree);

            mTitle.setText(mCurrentFolder.getName());

        } else {
            mNavBars.setImageDrawable(mContext.getDrawable(R.drawable.ic_nav_orange));

            mNavBars.setOnClickListener(showNavigation);
            mNavLogo.setOnClickListener(showNavigation);

            mNavLogo.setImageDrawable(mContext.getDrawable(R.drawable.ic_logo_g_orange));
            mNavBars.setVisibility(View.VISIBLE);
        }

        mNavLogo.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);

        mHelper = new DataHelper();

        List<ListItem> data = mHelper.CombineLibraryItems(mCurrentFolder.getDocuments(), mCurrentFolder.getFolders(), mCurrentFolder.getParent());

        new LibraryTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent())
                .BuildAdapter(data,  mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);
    }



    protected View.OnClickListener showNavigation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MainActivity)getActivity()).openNavigationDrawer();
        }
    };

    protected View.OnClickListener navigateUpTree = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().popBackStack();
        }
    };


}
