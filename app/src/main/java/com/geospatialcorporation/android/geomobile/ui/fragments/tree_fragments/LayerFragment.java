package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerFolderPanelFragment;
import com.geospatialcorporation.android.geomobile.models.ListItem;

import java.util.List;

import butterknife.OnClick;

public class LayerFragment extends RecyclerTreeFragment implements IContentRefresher, IPostExecuter<Folder>, IFragmentView {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    ProgressDialogHelper mProgressDialogHelper;

    @Nullable
    @OnClick(R.id.OptionsFab)
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

    @Nullable
    @OnClick(R.id.landOptionsIV)
    public void optionsDropDownland(){
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
        setView(inflater, container, R.layout.fragment_recyclertree);
        mContext = getActivity();

        mTitle.setText("Loading...");
        mInfo.setVisibility(View.INVISIBLE);

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        application.setLayerFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_FRAGMENT).hide().build();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.white));


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
        int folderId = 0;

        if(args != null) {
            folderId = args.getInt(Folder.FOLDER_INTENT, 0);
        }

        mProgressHelper = new ProgressDialogHelper(mContext);
        mProgressHelper.toggleProgressDialog();

        task.getByFolder(new GetLayersByFolderTaskParams(getFragmentManager(), mContext, this), folderId);
    }

    @Override
    protected void buildRecycler() {
        mHelper = new DataHelper();

        List<ListItem> data = mHelper.CombineLayerItems(mCurrentFolder.getLayers(), mCurrentFolder.getFolders(), mCurrentFolder.getParent());

        new LayerTreeSectionBuilder(mContext, getFragmentManager(), mCurrentFolder.getParent(), mPanelManager)
                .BuildAdapter(data, mCurrentFolder.getFolders().size())
                .setRecycler(mRecyclerView);
    }

    @Override
    public void setDetailFrame(View view, FragmentManager fm) {
        setFrame(R.id.detail_frame, fm);
    }

    @Override
    public void setContentFragment(FragmentManager fm) {
        setFrame(R.id.content_frame, fm);
    }

    private void setFrame(int frame, FragmentManager fm){
        fm.beginTransaction()
                .replace(frame, this)
                .addToBackStack(null)
                .commit();
    }

}