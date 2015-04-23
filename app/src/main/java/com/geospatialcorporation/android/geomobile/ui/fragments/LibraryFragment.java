package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryFragment extends Fragment{
    protected static final String TAG = LibraryFragment.class.getSimpleName();

    //region Properties
    private List<Folder> mFolders;
    private View mRootView;
    private TreeService mService;
    private DataHelper mHelper;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHelper = new DataHelper();

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        ButterKnife.inject(this, mRootView);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.libraryitem_recyclerView);

        mService = application.getRestAdapter().create(TreeService.class);

        new GetDocumentsTask().execute();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    private class GetDocumentsTask extends AsyncTask<Void, Void, List<Folder>> {

        @Override
        protected List<Folder> doInBackground(Void... params) {
            try {
                List<Folder> folders = mService.getLibrary();

                mFolders = mHelper.GetFoldersRecursively(folders.get(0));
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 75");
                }
            }

            return mFolders;
        }

        @Override
        protected void onPostExecute(List<Folder> folders){
            List<ListItem> data = mHelper.CombineLibraryItems(new ArrayList<Layer>(), folders);

            ListItemAdapter adapter = new ListItemAdapter(getActivity(), data);

            mRecyclerView.setAdapter(adapter);
        }

    }

}
