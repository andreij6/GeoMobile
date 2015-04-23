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
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

public class LibraryFragment extends Fragment{
    protected static final String TAG = LibraryFragment.class.getSimpleName();

    //region Properties
    private List<Folder> mFolders;
    private TreeService mService;
    private FolderService mFolderService;
    private DataHelper mHelper;
    private List<Document> mDocuments;
    private Context mContext;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;

        mHelper = new DataHelper();

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        ButterKnife.inject(this, mRootView);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.libraryitem_recyclerView);

        mService = application.getRestAdapter().create(TreeService.class);
        mFolderService = application.getRestAdapter().create(FolderService.class);

        mContext = getActivity();

        new GetDocumentsTask().execute();

        return mRootView;
    }

    private class GetDocumentsTask extends AsyncTask<Void, Void, List<Folder>> {

        @Override
        protected List<Folder> doInBackground(Void... params) {
            try {
                List<Folder> folders = mService.getLibrary();
                Folder currentFolder = folders.get(0);
                mFolders = currentFolder.getFolders();

                List<Folder> allfolders = mHelper.GetFoldersRecursively(currentFolder);
                List<Document> documents = mFolderService.getFolderDocuments(currentFolder.getId());

                application.setDocumentFolders(allfolders);
                application.setDocuments(documents);

                mDocuments = documents;

            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 75");
                }
            }

            return mFolders;
        }

        @Override
        protected void onPostExecute(List<Folder> folders){

            SectionTreeBuilder builder = new SectionTreeBuilder(mContext)
                    .AddLibraryData(mDocuments, folders)
                    .BuildAdapter(ListItemAdapter.LIBRARY)
                    .setRecycler(mRecyclerView);
        }

    }

}
