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
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
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
    private FolderService mFolderService;
    private DataHelper mHelper;
    RecyclerView.LayoutManager mLM;
    private List<Document> mDocuments;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHelper = new DataHelper();

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        ButterKnife.inject(this, mRootView);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.libraryitem_recyclerView);

        mService = application.getRestAdapter().create(TreeService.class);
        mFolderService = application.getRestAdapter().create(FolderService.class);

        new GetDocumentsTask().execute();

        mLM = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(mLM);

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
            List<ListItem> data = mHelper.CombineLibraryItems(mDocuments, folders);

            ListItemAdapter adapter = new ListItemAdapter(getActivity(), data, ListItemAdapter.LIBRARY);

            List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Folders"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(folders.size(), "Documents"));

            SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

            SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                    SimpleSectionedRecyclerViewAdapter(getActivity(),  R.layout.section, R.id.section_text, adapter);

            mSectionedAdapter.setSections(sections.toArray(dummy));

            mRecyclerView.setAdapter(mSectionedAdapter);

            mRecyclerView.setLayoutManager(mLM);
        }

    }

}
