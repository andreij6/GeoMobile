package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

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
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersByFolderTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LayerTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerFolderPanelFragment;

import java.util.List;

import butterknife.OnClick;

public class LayerFragment extends RecyclerTreeFragment implements IContentRefresher, IPostExecuter<Folder>, IFragmentView {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    ProgressDialogHelper mProgressDialogHelper;
    int mFolderId;
    
    @Nullable
    @OnClick(R.id.OptionsFab)
    public void optionsDropDown(){

        options();
    }

    protected void options() {
        if(!mPanelManager.getIsOpen()){

            Fragment f = new LayerFolderPanelFragment();

            f.setArguments(mCurrentFolder.toBundle());

            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            if(mIsLandscape){
                mPanelManager.expand();
            } else {
                mPanelManager.halfAnchor();
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
        setView(inflater, container, R.layout.fragment_recyclertree);
        mContext = getActivity();

        mTitle.setText("Loading...");
        mInfo.setVisibility(View.INVISIBLE);

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        application.setLayerFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_FRAGMENT).hide().build();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mContext, R.color.white));


        if(mIsLandscape){
            mParentFolder.setText(getString(R.string.layer_tree));
        }

        handleArguments();

        if(mOptionsSlider != null) {
            mOptionsSlider.bringToFront();
        }

        return mView;
    }

    @Override
    public void handleArguments() {
        Bundle args = getArguments();
        IGetLayersTask task = application.getTasksComponent().provideLayersTask();
        mFolderId = 0;

        if(args != null) {
            mFolderId = args.getInt(Folder.FOLDER_INTENT, 0);
        }

        mProgressHelper = new ProgressDialogHelper(mContext);
        mProgressHelper.toggleProgressDialog();

        task.getByFolder(new GetLayersByFolderTaskParams(getFragmentManager(), mContext, this), mFolderId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Folder.FOLDER_INTENT, mFolderId);
    }

    @Override
    protected void buildRecycler() {
        mHelper = new DataHelper();

        List<ListItem> data = mHelper.CombineLayerItems(mCurrentFolder.getLayers(), mCurrentFolder.getFolders());

        new LayerTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent(), mPanelManager)
                .BuildAdapter(data, mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);
    }

    @Override
    public void setDetailFrame(View view, FragmentManager fm, Bundle bundle) {
        setFrame(R.id.detail_frame, fm, bundle);
    }

    @Override
    public void setContentFragment(FragmentManager fm, Bundle bundle) {
        setFrame(R.id.content_frame, fm, bundle);
    }

    private void setFrame(int frame, FragmentManager fm, Bundle bundle){
        Fragment fragment = new LayerFragment();

        if(bundle != null){
            fragment.setArguments(bundle);
        }

        fm.beginTransaction()
                .replace(frame, this)
                .addToBackStack(null)
                .commit();
    }

}