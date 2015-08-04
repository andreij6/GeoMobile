package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LibraryTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
    IGeneralDialog mDialog;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.fab)
    @SuppressWarnings("unused")
    public void libraryActionOnClick(){

        mDialog = application.getUIHelperComponent().provideGeneralDialog();

        mDialog.libraryAction(mCurrentFolder, getActivity(), getFragmentManager());
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
        View mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        ButterKnife.inject(this, mRootView);
        mContext = getActivity();

        sendScreenName();

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        mDocumentsTask = application.getTasksComponent().provideGetDocumentsTask();
        handleArguments();

        return mRootView;
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
            SetTitle(mCurrentFolder.getName());
        } else {
            SetTitle(R.string.library_fragment_title);
        }

        mHelper = new DataHelper();

        List<ListItem> data = mHelper.CombineLibraryItems(mCurrentFolder.getDocuments(), mCurrentFolder.getFolders(), mCurrentFolder.getParent());

        new LibraryTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent())
                .BuildAdapter(data,  mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);
    }
}
