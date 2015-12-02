package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.MediaConstants;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LibraryTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.library.services.Library.ILibraryProcessor;
import com.geospatialcorporation.android.geomobile.library.services.Library.LibraryProcessor;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabDocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLibraryFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TabLibraryFragment extends TabGeoViewFragmentBase
        implements IContentRefresher,
        IPostExecuter<Folder>, IPanelFragmentCtrl
    {

    private static final String TAG = TabLibraryFragment.class.getSimpleName();

    ILibraryProcessor mProcessor;
    Folder mCurrentFolder;

    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Bind(R.id.goBackIV) ImageView mGoBack;
    @Bind(R.id.OptionsFab) ImageView mOptions;
    @Bind(R.id.title) TextView mTitle;

    @SuppressWarnings("unused")
    @OnClick(R.id.goBackIV)
    public void goUpOne(){

        Fragment fragment = new TabLibraryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Folder.FOLDER_INTENT, mCurrentFolder.getParent().getId());

        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.info_frame, fragment)
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.folderInformation)
    public void info(){
        goToFolderDetail();
    }

    @OnClick(R.id.title)
    public void titleClick(){
        goToFolderDetail();
    }

    protected void goToFolderDetail() {
        Fragment f = new TabDocumentFolderDetailFragment();

        f.setArguments(mCurrentFolder.toBundle());

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.info_frame, f)
                .commit();
    }

        @SuppressWarnings("unused")
    @OnClick(R.id.OptionsFab)
    public void openOptions(){
        mProcessor.onOptionsButtonPressed(mCurrentFolder, getFragmentManager(), new TabletLibraryFolderPanelFragment());
    }

    @OnClick(R.id.close)
    public void closeFragment(){
        ((MainTabletActivity)getActivity()).closeInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mGoBack.setVisibility(View.INVISIBLE);

        mProcessor = new LibraryProcessor.Builder().progressDialog(getActivity())
                            .panel(mPanel).swipeRefresh(mSwipeRefreshLayout, this, getActivity())
                            .build();

        mProcessor.handleArgs(getArguments(), new GetDocumentsParam(getFragmentManager(), mCurrentFolder, getActivity(), this));

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == Activity.RESULT_OK) {

            IDocumentTreeService uploader = mProcessor.getDocumentUploader();

            if (requestCode == MediaConstants.PICK_FILE_REQUEST) {
                // Make sure the request was successful

                uploader.sendDocument(mCurrentFolder, data.getData(), null);

            }

            if(requestCode == MediaConstants.PICK_IMAGE_REQUEST){

                uploader.sendPickedImage(mCurrentFolder, data.getData(), null);

            }

            if(requestCode == MediaConstants.TAKE_IMAGE_REQUEST) {

                uploader.sendTakenImage(mCurrentFolder, application.mMediaUri, null);

            }
        }


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.library_tab_fragment;
    }

    @Override
    public void refresh() {
        mProcessor.handleArgs(getArguments(), new GetDocumentsParam(getFragmentManager(), mCurrentFolder, getActivity(), this));
    }

    @Override
    public void onPostExecute(Folder model) {
        mCurrentFolder = model;

        if(mCurrentFolder == null){
            return;
        }

        if(!mCurrentFolder.isEditable()){
            mOptions.setVisibility(View.GONE);
        }

        if(mCurrentFolder.getParent() != null){
            //has a folder to navigate up to
            mGoBack.setVisibility(View.VISIBLE);
            mGoBack.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_chevron_left_grey600_36dp));

            mTitle.setText(mCurrentFolder.getProperName());
        } else {
            mTitle.setText(mCurrentFolder.getProperName());
        }

        List<ListItem> data = mProcessor.getListItemData(mCurrentFolder);

        new LibraryTreeSectionBuilder(getActivity(), getFragmentManager(), mCurrentFolder.getParent(), mProcessor.getPanelManager())
                .BuildAdapter(data,  mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);

        mProcessor.hideProgressDialog();

    }

    @Override
    public void closePanel() {
        mProcessor.closePanel();
    }
}
