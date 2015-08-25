package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents.DocumentsAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetAllDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapFeatureDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.MoveDocumentDialogFragment;

import java.util.List;

import retrofit.RetrofitError;


public class DocumentsTask implements IGetDocumentsTask {

    private static final String TAG = DocumentsTask.class.getSimpleName();

    IAddDataRepository<Document> mDocumentRepo;
    IAddDataRepository<Folder> mFolderRepo;
    DataHelper mHelper;
    IFolderTreeService mFolderTreeService;
    TreeService mTreeService;

    public DocumentsTask(){
        mFolderRepo = new FolderAppSource();
        mDocumentRepo = new DocumentsAppSource();
        mHelper = new DataHelper();
        mFolderTreeService = application.getTreeServiceComponent().provideFolderTreeService();
        mTreeService = application.getRestAdapter().create(TreeService.class);
    }

    @Override
    public void getDocumentsByFolderId(GetDocumentsParam param, Integer folderId) {
        new GetDocumentsByFolderIdTask(param).execute(folderId);
    }

    @Override
    public void getAllDocuments(GetAllDocumentsParam param) {
        new GetDocumentsTask(param).execute();
    }

    @Override
    public void getDocumentFolders(MoveDocumentDialogFragment fragment) {
        new GetDocumentFoldersTask(fragment).execute();
    }

    //TODO: use GeoAsyncTask
    protected class GetDocumentsByFolderIdTask extends GeoAsyncTask<Integer, Void, Folder> {

        FragmentManager mFragmentManager;
        Folder mCurrentFolder;
        Context mContext;
        ISlidingPanelManager mPanelManager;

        public GetDocumentsByFolderIdTask(GetDocumentsParam param){
            super(param.getFragment());
            mFragmentManager = param.getFragmentManager();
            mCurrentFolder = param.getCurrentFolder();
            mContext = param.getContext();
        }

        @Override
        protected Folder doInBackground(Integer... params) {
            try {
                Boolean getAll = false;

                if (params[0] == 0) {
                    getAll = true;
                    List<Folder> documentsTree = mTreeService.getDocuments();
                    mCurrentFolder = documentsTree.get(0);
                } else {
                    mCurrentFolder = mFolderTreeService.getById(params[0]);
                }

                if (mCurrentFolder == null)
                    throw new Exception("mCurrentFolder is null exception.");

                if (getAll) {
                    List<Document> allDocuments = mHelper.getDocumentsRecursively(mCurrentFolder);
                    List<Folder> allFolders = mHelper.getFoldersRecursively(mCurrentFolder, mCurrentFolder.getParent());
                    mFolderRepo.Add(allFolders);
                    mDocumentRepo.Add(allDocuments);
                }

                List<Folder> folders = mFolderTreeService.getFoldersByFolder(mCurrentFolder.getId(), false);
                List<Document> documents = mFolderTreeService.getDocumentsByFolder(mCurrentFolder.getId());
                mDocumentRepo.Add(documents);

                mCurrentFolder.setFolders(folders);
                mCurrentFolder.setDocuments(documents);
            } catch (RetrofitError e) {
                Log.d(TAG, "Messed up.");
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return mCurrentFolder;
        }

    }

    protected class GetDocumentsTask extends AsyncTask<Void, Void, List<Document>> {

        MapFeatureDocumentDialogFragment mFragment;
        List<Document> mDocuments;

        public GetDocumentsTask(GetAllDocumentsParam param){
            mDocuments = param.getDocuments();
            mFragment = param.getFragment();
        }

        @Override
        protected List<Document> doInBackground(Void... params) {
            List<Folder> folders = mTreeService.getDocuments();

            List<Folder> allDocFolders = mHelper.getFoldersRecursively(folders.get(0), null);

            for(Folder folder : allDocFolders){
                mDocuments = mFolderTreeService.getDocumentsByFolder(folder.getId());
            }

            return mDocuments;
        }

        @Override
        protected void onPostExecute(List<Document> docs){
            mFragment.addItemsOnSpinner(docs);
            mFragment.addListenerOnSpinnerItemSelection();
        }
    }

    protected class GetDocumentFoldersTask extends AsyncTask<Void, Void, List<Folder>> {

        MoveDocumentDialogFragment mFragment;

        public GetDocumentFoldersTask(MoveDocumentDialogFragment context){
            mFragment = context;
        }

        @Override
        protected List<Folder> doInBackground(Void... params) {
            List<Folder> folders = mTreeService.getDocuments();

            return mHelper.getFoldersRecursively(folders.get(0), null);
        }

        @Override
        protected void onPostExecute(List<Folder> folders){
            mFragment.addItemsOnSpinner(folders);
            mFragment.addListenerOnSpinnerItemSelection();
        }
    }
}
