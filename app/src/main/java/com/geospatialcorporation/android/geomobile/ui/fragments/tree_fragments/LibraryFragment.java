package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LibraryTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.RestoreSettings;
import com.geospatialcorporation.android.geomobile.ui.activity.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LibraryFolderPanelFragment;

import java.util.List;

import butterknife.OnClick;

public class LibraryFragment extends RecyclerTreeFragment
        implements IContentRefresher, IFragmentView, IPostExecuter<Folder> {
    protected static final String TAG = LibraryFragment.class.getSimpleName();
    int mFolderId;

    //region Properties & Onclicks
    IGetDocumentsTask mDocumentsTask;
    IDocumentTreeService mUploader;

    LibraryRestoreSettings mRestoreSettings;
    static String RESTORE_SETTINGS_KEY = LibraryRestoreSettings.class.getSimpleName();


    @Nullable
    @OnClick(R.id.OptionsFab)
    public void optionsDropDown(){
        options();
    }

    protected void options() {
        if(!mPanelManager.getIsOpen()){

            Fragment f = new LibraryFolderPanelFragment();

            f.setArguments(mCurrentFolder.toBundle());

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            if(mIsLandscape){
                mPanelManager.expand();
            } else {
                mPanelManager.halfAnchor(0.1f);
            }
            mPanelManager.touch(false);
        } else {
            closePanel();
        }
    }

    @Nullable
    @OnClick(R.id.landOptionsIV)
    public void optionsDropDownland(){
        options();
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
        mSavedInstanceState = savedInstanceState;
        mContext = getActivity();

        mTitle.setText("Loading...");
        mInfo.setVisibility(View.INVISIBLE);

        sendScreenName();

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();
        mProgressHelper = new ProgressDialogHelper(getActivity());

        application.setLibraryFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LIBRARY_FRAGMENT).hide().build();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mContext, R.color.white));

        if(mIsLandscape){
            mParentFolder.setText(getString(R.string.library_tree));
        }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRestoreSettings != null){
            mRestoreSettings.onSaveInstance(mFolderId, RESTORE_SETTINGS_KEY);
        } else {
            application.setRestoreSettings(LibraryRestoreSettings.newInstance(mFolderId), RESTORE_SETTINGS_KEY);
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

        List<ListItem> data = mHelper.CombineLibraryItems(mCurrentFolder.getDocuments(), mCurrentFolder.getFolders());

        new LibraryTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent(), mPanelManager)
                .BuildAdapter(data, mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);
    }

    protected void sendScreenName() {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().LibraryTreeScreen());
    }

    @Override
    protected String getTreeName() {
        return getString(R.string.library_tree);
    }

    @Override
    public Fragment getNewInstanceOfCurrentFragment() {
        return new LibraryFragment();
    }

    @Override
    public void handleArguments() {
        Bundle args = getArguments();

        mDocumentsTask = application.getTasksComponent().provideGetDocumentsTask();

        mFolderId = -1;

        if(args != null) {
            mFolderId = args.getInt(Folder.FOLDER_INTENT, -1);
        }

        if(mSavedInstanceState == null){
            mRestoreSettings = (LibraryRestoreSettings) application.getRestoreSettings(RESTORE_SETTINGS_KEY);
            if (mRestoreSettings != null && mFolderId == -1) {
                mFolderId = mRestoreSettings.getEntityConditionally(mFolderId);
            }

            mProgressHelper.showProgressDialog();
            mDocumentsTask.getDocumentsByFolderId(new GetDocumentsParam(getFragmentManager(), mCurrentFolder, mContext, this), mFolderId);
        }

    }

    @Override
    public void setContentFragment(FragmentManager fm, Bundle bundle) {
        //application.getMainActivity().onNavigationDrawerItemSelected(MainNavCtrl.ViewConstants.LIBRARY);

        setFrame(R.id.content_frame, fm, bundle);
    }

    private void setFrame(int frame, FragmentManager fm, Bundle bundle){
        LibraryFragment fragment = new LibraryFragment();

        if(bundle != null){
            fragment.setArguments(bundle);
        }

        fm.beginTransaction()
                .replace(frame, this)
                .addToBackStack(null)
                .commit();
    }

    public static class LibraryRestoreSettings extends RestoreSettings<Integer> {

        public static LibraryRestoreSettings newInstance(int folderId) {
            LibraryRestoreSettings result = new LibraryRestoreSettings();
            result.setShouldRestore(true);
            result.setEntity(folderId);
            return result;
        }


    }
}
