package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents.DocumentsAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.SectionTreeBuilder;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.LibraryActionDialogFragment;
import com.melnykov.fab.FloatingActionButton;


import java.util.List;
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
    @InjectView(R.id.fab) FloatingActionButton mCircleButton;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;


    @OnClick(R.id.fab)
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
        mContext = getActivity();
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        mTreeService = application.getRestAdapter().create(TreeService.class);
        mFolderTreeService = new FolderTreeService();


        handleArguments();

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.document_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == MainActivity.MediaConstants.PICK_FILE_REQUEST) {
                // Make sure the request was successful

                DocumentTreeService uploader = new DocumentTreeService();
                uploader.SendDocument(mCurrentFolder, data.getData());

            }

            if(requestCode == MainActivity.MediaConstants.PICK_IMAGE_REQUEST){

                DocumentTreeService uploader = new DocumentTreeService();
                uploader.SendPickedImage(mCurrentFolder, data.getData());

            }

            if(requestCode == MainActivity.MediaConstants.TAKE_IMAGE_REQUEST) {

                DocumentTreeService uploader = new DocumentTreeService();
                uploader.SendTakenImage(mCurrentFolder, application.mMediaUri);

            }
        }


    }



    private class GetDocumentsTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Boolean getAll = false;
                IAddDataRepository<Document> documentRepo = new DocumentsAppSource();
                IAddDataRepository<Folder> folderRepo = new FolderAppSource();
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
                    folderRepo.Add(allFolders);
                    documentRepo.Add(allDocuments);
                }

                List<Folder> folders = mFolderTreeService.getFoldersByFolder(mCurrentFolder.getId(), false);
                List<Document> documents = mFolderTreeService.getDocumentsByFolder(mCurrentFolder.getId());
                documentRepo.Add(documents);

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

    private void handleArguments() {
        Bundle args = getArguments();

        if(args != null) {

            int folderId = args.getInt(Folder.FOLDER_INTENT, 0);

            new GetDocumentsTask().execute(folderId);
        } else {
            firstDocumentView();
        }

    }

    private class DocumentRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

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
