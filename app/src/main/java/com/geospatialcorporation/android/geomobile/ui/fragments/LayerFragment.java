package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.IAppDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.SectionTreeBuilder;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.LayerActionDialogFragment;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;

public class LayerFragment extends Fragment {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    private Folder mCurrentFolder;
    private TreeService mTreeService;
    private FolderTreeService mFolderTreeService;
    private LayerService mLayerService;
    private DataHelper mHelper;
    private Context mContext;

    RecyclerView.LayoutManager mLM;

    public LayerFragment() {
        mHelper = new DataHelper();
    }

    @InjectView(R.id.layer_recyclerView) RecyclerView mRecycler;
    @InjectView(R.id.fab) FloatingActionButton mCircleButton;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.fab)
    @SuppressWarnings("unused")
    public void layerActionClick(){
        LayerActionDialogFragment l = new LayerActionDialogFragment();

        l.setContext(getActivity());
        l.setFolder(mCurrentFolder);
        l.show(getFragmentManager(), "layer actions");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View mRootView = inflater.inflate(R.layout.fragment_layeritems, container, false);

        ButterKnife.inject(this, mRootView);
        mContext = getActivity();
        mSwipeRefreshLayout.setOnRefreshListener(new LayerRefreshLayout());
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        mTreeService = application.getRestAdapter().create(TreeService.class);
        mLayerService = application.getRestAdapter().create(LayerService.class);
        mFolderTreeService = new FolderTreeService();


        handleArguments();

        return mRootView;
    }

    private class GetLayersTask extends AsyncTask<Integer, Void, Folder> {
        @Override
        protected Folder doInBackground(Integer... params) {
            try {
                if (params[0] == 0) {
                    List<Folder> folders = mTreeService.getLayers();

                    mCurrentFolder = folders.get(0);
                    getCurrentFolderLayers();
                } else {
                    mCurrentFolder = mFolderTreeService.getFolderById(params[0]);
                }



                mCurrentFolder.setFolders(mFolderTreeService.getFoldersByFolder(mCurrentFolder.getId(), false));
                mCurrentFolder.setLayers(mFolderTreeService.getLayersByFolder(mCurrentFolder.getId(), false));
            } catch (RetrofitError e) {
                Log.d(TAG, "Messed up.");
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return mCurrentFolder;
        }

        private void getCurrentFolderLayers() {
            List<Folder> allFolders = mHelper.getFoldersRecursively(mCurrentFolder, mCurrentFolder.getParent());
            List<Layer> allLayers = mLayerService.getLayers();

            if (allFolders.size() > 0) {
                IAddDataRepository<Folder> repo = new FolderAppSource();
                repo.Add(allFolders);
            }
            else { Log.d(TAG, "allFolders empty."); }
            if (allLayers.size() > 0) {
                IAppDataRepository<Layer> LayerRepo = new LayersAppSource();
                LayerRepo.Add(allLayers);
            }
            else { Log.d(TAG, "allLayers empty."); }
        }

        @Override
        protected void onPostExecute(Folder rootFolder) {
            new SectionTreeBuilder(mContext, getFragmentManager())
                    .AddLayerData(mCurrentFolder.getLayers(), mCurrentFolder.getFolders(), mCurrentFolder.getParent())
                    .BuildAdapter(ListItemAdapter.LAYER)
                    .setRecycler(mRecycler);
        }
    }

    private void firstLayerView() {
        new GetLayersTask().execute(0);
    }

    private void handleArguments() {
        Bundle args = getArguments();

        if(args != null) {
            int folderId = args.getInt(Folder.FOLDER_INTENT, 0);
            new GetLayersTask().execute(folderId);
        } else {
            firstLayerView();
        }
    }

    private class LayerRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleArguments();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }

    }

}