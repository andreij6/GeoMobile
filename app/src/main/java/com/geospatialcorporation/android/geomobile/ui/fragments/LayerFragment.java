package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.SectionTreeBuilder;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class LayerFragment extends Fragment {
    protected final static String TAG = LayerFragment.class.getSimpleName();

    private Folder mCurrentFolder;
    private TreeService mTreeService;
    private FolderService mFolderService;
    private LayerService mLayerService;
    private DataHelper mHelper;
    private Context mContext;

    RecyclerView.LayoutManager mLM;

    public LayerFragment() {
        mHelper = new DataHelper();
    }

    @InjectView(R.id.layer_recyclerView)
    RecyclerView mRecycler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View mRootView = inflater.inflate(R.layout.fragment_layeritems, container, false);

        ButterKnife.inject(this, mRootView);

        mTreeService = application.getRestAdapter().create(TreeService.class);
        mFolderService = application.getRestAdapter().create(FolderService.class);
        mLayerService = application.getRestAdapter().create(LayerService.class);

        mContext = getActivity();

        Bundle args = getArguments();
        if (args != null) {
            handleArguments(args);
        } else {
            firstLayerView();
        }

        return mRootView;
    }

    private class GetLayersTask extends AsyncTask<Integer, Void, Folder> {
        @Override
        protected Folder doInBackground(Integer... params) {
            try {
                if (params[0] == 0) {
                    List<Folder> folders = mTreeService.getLayers();

                    mCurrentFolder = folders.get(0);

                    List<Folder> allFolders = mHelper.getFoldersRecursively(mCurrentFolder);
                    List<Layer> allLayers = mLayerService.getLayers();

                    if (allFolders.size() > 0) { application.setLayerFolders(allFolders); }
                    else { Log.d(TAG, "allFolders empty."); }
                    if (allLayers.size() > 0) { application.setLayers(allLayers); }
                    else { Log.d(TAG, "allLayers empty."); }
                } else {
                    mCurrentFolder = mFolderService.getFolderById(params[0]);
                }

                mCurrentFolder.setFolders(mFolderService.getFoldersByFolder(mCurrentFolder.getId()));
                mCurrentFolder.setLayers(mFolderService.getLayersByFolder(mCurrentFolder.getId()));
            } catch (RetrofitError e) {
                Log.d(TAG, "Messed up.");
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return mCurrentFolder;
        }

        @Override
        protected void onPostExecute(Folder rootFolder) {
            new SectionTreeBuilder(mContext)
                    .AddLayerData(mCurrentFolder.getLayers(), mCurrentFolder.getFolders(), mCurrentFolder.getParent())
                    .BuildAdapter(ListItemAdapter.LAYER)
                    .setRecycler(mRecycler);
        }
    }

    private void firstLayerView() {
        new GetLayersTask().execute(0);
    }

    private void handleArguments(Bundle args) {
        int folderId = args.getInt(Folder.FOLDER_INTENT, 0);
        new GetLayersTask().execute(folderId);
    }

}