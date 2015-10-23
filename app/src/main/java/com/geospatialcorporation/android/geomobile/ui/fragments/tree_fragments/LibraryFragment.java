package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LibraryTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LibraryFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class LibraryFragment extends GeoViewFragmentBase
        implements IContentRefresher, IPostExecuter<Folder> {
    protected static final String TAG = LibraryFragment.class.getSimpleName();

    //region Properties
    private Folder mCurrentFolder;
    private Context mContext;
    IGetDocumentsTask mDocumentsTask;
    IDocumentTreeService mUploader;
    DataHelper mHelper;
    ProgressDialogHelper mProgressHelper;

    @Bind(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.libraryOptionsIV) FloatingActionButton mOptionsSlider;
    @Bind(R.id.showNavIV1) ImageView mNavBars;
    @Bind(R.id.showNavIV2) ImageView mNavLogo;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.folderInformation) ImageView mInfo;
    @Bind(R.id.backFolder) TextView mParentFolder;
    //endregion

    //region OnClicks
    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment pageFragment = new GoogleMapFragment();

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, pageFragment)
                    .addToBackStack(null).commit();
        }
    }

    @OnClick(R.id.libraryOptionsIV)
    public void optionsDropDown(){
        if(!mPanelManager.getIsOpen()){

            Fragment f = new LibraryFolderPanelFragment();

            f.setArguments(mCurrentFolder.toBundle());

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor(0.1f);
            mPanelManager.touch(false);
        } else {
            closePanel();
        }
    }

    @OnClick(R.id.folderInformation)
    public void folderInfo(){
        Fragment f = new DocumentFolderDetailFragment();

        f.setArguments(mCurrentFolder.toBundle());

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, f)
                .commit();
    }

    @OnClick(R.id.title)
    public void titleClicked(){
        if(mPanelManager.getIsOpen()) {
            closePanel();
        }
    }
    //endregion

    //region Fragment Overrides
    @Override
    public void onCreate(Bundle savedInstance) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView(inflater, container, R.layout.fragment_libraryitems);
        mContext = getActivity();

        mTitle.setText("Loading...");

        mInfo.setVisibility(View.INVISIBLE);

        sendScreenName();

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();
        mProgressHelper = new ProgressDialogHelper(getActivity());

        application.setLibraryFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LIBRARY_FRAGMENT).hide().build();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.white));

        mProgressHelper.showProgressDialog();
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

                mUploader.sendDocument(mCurrentFolder, data.getData(), null);

            }

            if(requestCode == MainActivity.MediaConstants.PICK_IMAGE_REQUEST){

                mUploader.sendPickedImage(mCurrentFolder, data.getData(), null);

            }

            if(requestCode == MainActivity.MediaConstants.TAKE_IMAGE_REQUEST) {

                mUploader.sendTakenImage(mCurrentFolder, application.mMediaUri, null);

            }
        }


    }
    //endregion

    //region View.OnClicks
    protected View.OnClickListener showNavigation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPanelManager.getIsOpen()) {
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
    //endregion

    @Override
    public void onPostExecute(Folder model) {
        mCurrentFolder = model;

        mPanelManager.hide();

        if(mCurrentFolder.getParent() != null){
            mNavBars.setVisibility(View.GONE);
            mNavLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_chevron_left_grey600_18dp));
            mNavLogo.setPadding(-12, 0, 0, 0);

            mNavBars.setOnClickListener(navigateUpTree);
            mNavLogo.setOnClickListener(navigateUpTree);
            mParentFolder.setOnClickListener(navigateUpTree);

            mParentFolder.setVisibility(View.VISIBLE);
            mParentFolder.setText(mCurrentFolder.getParent().getProperName());

        } else {
            mNavBars.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_nav_orange));

            mNavBars.setOnClickListener(showNavigation);
            mNavLogo.setOnClickListener(showNavigation);

            mNavLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_g_logo));
            mNavBars.setVisibility(View.VISIBLE);
        }

        mTitle.setText(mCurrentFolder.getProperName());

        mInfo.setVisibility(View.VISIBLE);
        mNavLogo.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);

        mHelper = new DataHelper();

        List<ListItem> data = mHelper.CombineLibraryItems(mCurrentFolder.getDocuments(), mCurrentFolder.getFolders(), mCurrentFolder.getParent());

        new LibraryTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent(), mPanelManager)
                .BuildAdapter(data,  mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);

        mProgressHelper.hideProgressDialog();
    }

    public void closePanel() {
        mPanelManager.hide();
    }

    public void goToFolderInfo() {
        Fragment f = new DocumentFolderDetailFragment();

        f.setArguments(mCurrentFolder.toBundle());

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, f)
                .commit();
    }

    protected void sendScreenName() {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().LibraryTreeScreen());
    }

    @Override
    public void refresh() {
        handleArguments();
    }

    protected void handleArguments() {
        Bundle args = getArguments();

        GetDocumentsParam params = new GetDocumentsParam(getFragmentManager(), mCurrentFolder, mContext, this);

        if(args != null) {
            int folderId = args.getInt(Folder.FOLDER_INTENT, 0);

            mDocumentsTask.getDocumentsByFolderId(params, folderId);


        } else {
            mDocumentsTask.getDocumentsByFolderId(params, 0);
        }

    }
}
