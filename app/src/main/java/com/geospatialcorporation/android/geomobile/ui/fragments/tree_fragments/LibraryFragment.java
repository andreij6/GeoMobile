package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations.MainNavCtrl;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.AccessLevelCodes;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LibraryTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.MainNavigationDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LibraryFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class LibraryFragment extends RecyclerTreeFragment
        implements IContentRefresher, IFragmentView, IPostExecuter<Folder> {
    protected static final String TAG = LibraryFragment.class.getSimpleName();

    //region Properties

    IGetDocumentsTask mDocumentsTask;
    IDocumentTreeService mUploader;

    @Nullable
    @OnClick(R.id.OptionsFab)
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

    @Nullable
    @OnClick(R.id.landOptionsIV)
    public void optionsDropDownland(){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView(inflater, container, R.layout.fragment_recyclertree);
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


        handleArguments();

        //fixes 4.3
        if(mOptionsSlider != null) {
            mOptionsSlider.bringToFront();
        }

        return mView;
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
    protected void buildRecycler() {
        mHelper = new DataHelper();

        List<ListItem> data = mHelper.CombineLibraryItems(mCurrentFolder.getDocuments(), mCurrentFolder.getFolders(), mCurrentFolder.getParent());

        new LibraryTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent(), mPanelManager)
                .BuildAdapter(data, mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);
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
    public void handleArguments() {
        Bundle args = getArguments();

        mProgressHelper.showProgressDialog();
        mDocumentsTask = application.getTasksComponent().provideGetDocumentsTask();

        GetDocumentsParam params = new GetDocumentsParam(getFragmentManager(), mCurrentFolder, mContext, this);

        if(args != null) {
            int folderId = args.getInt(Folder.FOLDER_INTENT, 0);

            mDocumentsTask.getDocumentsByFolderId(params, folderId);

        } else {
            mDocumentsTask.getDocumentsByFolderId(params, 0);
        }

    }

    @Override
    public void setDetailFrame(View view, FragmentManager fm) {
        view.performClick();
    }

    @Override
    public void setContentFragment(FragmentManager fm) {
        application.getMainActivity().onNavigationDrawerItemSelected(MainNavCtrl.ViewConstants.LIBRARY);
    }
}
