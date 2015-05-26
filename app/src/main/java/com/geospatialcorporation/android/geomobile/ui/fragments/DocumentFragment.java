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

public class DocumentFragment extends Fragment {
    protected static final String TAG = DocumentFragment.class.getSimpleName();

    //region Properties
    private List<Folder> mFolders;
    private TreeService mTreeService;
    private FolderService mFolderService;
    private DataHelper mHelper;
    private List<Document> mDocuments;
    private Folder mCurrentFolder;
    private Context mContext;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView)
    RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;

        mHelper = new DataHelper();

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        ButterKnife.inject(this, mRootView);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.libraryitem_recyclerView);

        mTreeService = application.getRestAdapter().create(TreeService.class);
        mFolderService = application.getRestAdapter().create(FolderService.class);

        mContext = getActivity();

        Bundle args = getArguments();
        if (args != null) {
            handleArguments(args);
        } else {
            firstDocumentView();
        }

        new GetDocumentsTask().execute();

        return mRootView;
    }

    private class GetDocumentsTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Boolean getAll = false;

                if (params[0] == 0) {
                    getAll = true;
                    List<Folder> folders = mTreeService.getDocuments();
                    mCurrentFolder = folders.get(0);
                } else {
                    mCurrentFolder = mFolderService.getFolderById(params[0]);
                }

                if (mCurrentFolder == null)
                    throw new Exception("mCurrentFolder is null exception.");

                if (getAll) {
                    List<Folder> allFolders = mHelper.getFoldersRecursively(mCurrentFolder);
                    application.setDocumentFolders(allFolders);
                }

                List<Folder> folders = mFolderService.getFoldersByFolder(mCurrentFolder.getId());
                List<Document> documents = mFolderService.getDocumentsByFolder(mCurrentFolder.getId());

                mFolders = folders;
                mCurrentFolder.setFolders(folders);
                mDocuments = documents;
                mCurrentFolder.setDocuments(documents);
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
                    .AddLibraryData(mDocuments, mFolders, mCurrentFolder.getParent())
                    .BuildAdapter(ListItemAdapter.LIBRARY)
                    .setRecycler(mRecyclerView);
        }

    }

    private Document getDocumentById(int id, List<Document> documents) {
        for (Document document : documents) {
            if (document.getId() == id) {
                return document;
            }
        }

        return null;
    }

    private void firstDocumentView() {
        new GetDocumentsTask().execute(0);
    }

    private void handleArguments(Bundle args) {
        int folderId = args.getInt(Folder.FOLDER_INTENT, 0);
        if (folderId != 0) {
            new GetDocumentsTask().execute(folderId);
        }
    }

}