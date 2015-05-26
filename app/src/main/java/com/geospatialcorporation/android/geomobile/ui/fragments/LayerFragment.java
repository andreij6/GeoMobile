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

    private List<Folder> mFolders;
    private TreeService mTreeService;
    private FolderService mFolderService;
    List<Layer> mLayers;
    Folder mCurrentFolder;
    DataHelper mHelper;
    Context mContext;

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

        mContext = getActivity();

        Bundle args = getArguments();
        if (args != null) {
            handleArguments(args);
        } else {
            firstLayerView();
        }

        return mRootView;
    }

    private class GetLayersTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Boolean getAll = false;

                if (params[0] == 0) {
                    getAll = true;
                    List<Folder> folders = mTreeService.getLayers();
                    mCurrentFolder = folders.get(0);
                } else {
                    mCurrentFolder = mFolderService.getFolderById(params[0]);
                }

                if (mCurrentFolder == null)
                    throw new Exception("mCurrentFolder is null exception.");

                if (getAll) {
                    List<Folder> allFolders = mHelper.getFoldersRecursively(mCurrentFolder);
                    List<Layer> allLayers = mHelper.getLayersRecursively(mCurrentFolder);

                    application.setLayerFolders(allFolders);
                    application.setLayers(allLayers);
                }

                List<Folder> folders = mFolderService.getFoldersByFolder(mCurrentFolder.getId());
                List<Layer> layers = mFolderService.getLayersByFolder(mCurrentFolder.getId());
                //mParent =

                mFolders = folders;
                mCurrentFolder.setFolders(folders);
                mLayers = layers;
                mCurrentFolder.setLayers(layers);
            } catch (RetrofitError e) {
                Log.d(TAG, "Messed up.");
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            new SectionTreeBuilder(mContext)
                    .AddLayerData(mLayers, mFolders, mCurrentFolder.getParent())
                    .BuildAdapter(ListItemAdapter.LAYER)
                    .setRecycler(mRecycler);
        }
    }

    private Folder getFolderById(int id, List<Folder> folders) {
        for (Folder folder : folders) {
            if (folder.getId() == id) {
                return folder;
            }
        }

        return null;
    }

    private void firstLayerView() {
        new GetLayersTask().execute(0);
    }

    private void handleArguments(Bundle args) {
        int folderId = args.getInt(Folder.FOLDER_INTENT, 0);
        new GetLayersTask().execute(folderId);
    }

}