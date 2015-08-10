package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersByFolderTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LayerTreeSectionBuilder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.LayerTreeActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LayerFragment extends GeoViewFragmentBase implements IContentRefresher, IPostExecuter<Folder> {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    private Context mContext;
    Folder mCurrentFolder;
    DataHelper mDataHelper;
    ProgressDialogHelper mProgressDialogHelper;

    @InjectView(R.id.layer_recyclerView) RecyclerView mRecycler;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.fab)
    @SuppressWarnings("unused")
    public void layerActionClick(){
        LayerTreeActionDialogFragment l = new LayerTreeActionDialogFragment();

        l.setContext(getActivity());
        l.setFolder(mCurrentFolder);
        l.show(getFragmentManager(), "layer actions");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(inflater, container, R.layout.fragment_layeritems);
        mContext = getActivity();

        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        handleArguments();

        mNavigationHelper.syncMenu(55);

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

        task.getByFolder(new GetLayersByFolderTaskParams(mRecycler, getFragmentManager(), mContext, this), folderId);

    }

    @Override
    public void onPostExecute(Folder currentFolder) {
        mCurrentFolder = currentFolder;

        if(currentFolder == null) { return; }

        if (currentFolder.getParent() != null) {
            SetTitle(currentFolder.getName());
        } else {
            SetTitle(R.string.layer_tree);
        }

        mDataHelper = new DataHelper();
        List<ListItem> data = mDataHelper.CombineLayerItems(currentFolder.getLayers(), currentFolder.getFolders(), currentFolder.getParent());

        mProgressDialogHelper.toggleProgressDialog();

        new LayerTreeSectionBuilder(mContext, getFragmentManager(), currentFolder.getParent())
                .BuildAdapter(data, currentFolder.getFolders().size())
                .setRecycler(mRecycler);
    }
}