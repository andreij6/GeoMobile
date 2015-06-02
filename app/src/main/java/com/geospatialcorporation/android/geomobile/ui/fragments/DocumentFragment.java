package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.SectionTreeBuilder;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.LibraryActionDialogFragment;

import java.util.List;

import at.markushi.ui.CircleButton;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;

public class DocumentFragment extends Fragment {
    protected static final String TAG = DocumentFragment.class.getSimpleName();

    //region Properties
    private List<Folder> mFolders;
    private TreeService mTreeService;
    private FolderTreeService mFolderTreeService;
    private DataHelper mHelper;
    private List<Document> mDocuments;
    private Folder mCurrentFolder;
    private Context mContext;
    //endregion

    @InjectView(R.id.libraryitem_recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.library_action_btn) CircleButton mCircleButton;

    @OnClick(R.id.library_action_btn)
    @SuppressWarnings("unused")
    public void libraryActionOnClick(){
        LibraryActionDialogFragment l = new LibraryActionDialogFragment();

        l.setContext(getActivity());
        l.setFolder(mCurrentFolder);
        l.show(getFragmentManager(), "library actions");
    }

    @Override
    public void onCreate(Bundle savedInstance){
        setHasOptionsMenu(true);
        super.onCreate(savedInstance);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;

        mHelper = new DataHelper();

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        ButterKnife.inject(this, mRootView);

        mTreeService = application.getRestAdapter().create(TreeService.class);
        mFolderTreeService = new FolderTreeService();

        mContext = getActivity();

        Bundle args = getArguments();
        if (args != null) {
            handleArguments(args);
        } else {
            firstDocumentView();
        }

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.document_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private class GetDocumentsTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Boolean getAll = false;

                if (params[0] == 0) {
                    getAll = true;
                    List<Folder> documentsTree = mTreeService.getDocuments();
                    mCurrentFolder = documentsTree.get(0);
                } else {
                    mCurrentFolder = mFolderTreeService.getFolderById(params[0]);
                }

                if (mCurrentFolder == null)
                    throw new Exception("mCurrentFolder is null exception.");

                if (getAll) {
                    List<Document> allDocuments = mHelper.getDocumentsRecursively(mCurrentFolder);
                    List<Folder> allFolders = mHelper.getFoldersRecursively(mCurrentFolder, mCurrentFolder.getParent());
                    application.setDocumentFolders(allFolders);
                    application.setDocuments(allDocuments);
                }

                List<Folder> folders = mFolderTreeService.getFoldersByFolder(mCurrentFolder.getId());
                List<Document> documents = mFolderTreeService.getDocumentsByFolder(mCurrentFolder.getId());
                application.setDocuments(documents);

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
            FragmentManager fm = getFragmentManager();
            new SectionTreeBuilder(mContext, fm)
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

        new GetDocumentsTask().execute(folderId);

    }



}
