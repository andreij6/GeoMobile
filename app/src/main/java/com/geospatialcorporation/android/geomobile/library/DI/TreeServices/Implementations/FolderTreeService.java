package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.FolderModifiedListener;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.List;

public class FolderTreeService implements IFolderTreeService {
    private static final String TAG = FolderTreeService.class.getSimpleName();

    FolderService mFolderService;
    IFullDataRepository<Folder> mFolderRepo;
    IFullDataRepository<Layer> mLayerRepo;
    TreeService mTreeService;

    public FolderTreeService(IFullDataRepository<Layer> layerRepo, IFullDataRepository<Folder> folderRepo){
        mFolderService = application.getRestAdapter().create(FolderService.class);
        mTreeService = application.getRestAdapter().create(TreeService.class);
        mLayerRepo = layerRepo;
        mFolderRepo = folderRepo;
    }

    @Override
    public Folder getById(int id) {
        Folder folder = mFolderRepo.getById(id);

        if(folder == null) {
            folder = mFolderService.getFolderById(id);
        }

        return folder;
    }

    @Override
    public List<Folder> getFoldersByFolder(int folderId, boolean checkCache) {
        Folder folder = mFolderRepo.getById(folderId);
        List<Folder> result;

        if(folder != null && folder.getFolders().size() > 0 && checkCache){
            result = folder.getFolders();
        } else {
            result = mFolderService.getFoldersByFolder(folderId);

            ((FolderAppSource)mFolderRepo).Add(result);
        }

        return result;
    }

    @Override
    public List<Layer> getLayersByFolder(int folderId, boolean checkCache) {
        return mFolderService.getLayersByFolder(folderId);
    }

    @Override
    public List<Document> getDocumentsByFolder(int folderId) {
        return mFolderService.getDocumentsByFolder(folderId);
    }

    @Override
    public void create(String name, int parentFolderId) {
        FolderCreateRequest createRequest = new FolderCreateRequest(name, parentFolderId);

        mFolderService.createFolder(createRequest, new RequestCallback<>(new FolderModifiedListener()));
    }

    @Override
    public void delete(int folderId) {
        mFolderService.remove(folderId, new RequestCallback<>(new FolderModifiedListener()));
    }

    @Override
    public void rename(int folderId, String name) {
        if(AuthorizedToRename(folderId)) {

            mFolderService.rename(folderId, new RenameRequest(name), new RequestCallback<>(new FolderModifiedListener()));
        } else {
            Toast.makeText(application.getAppContext(), "Not Authorized to Rename Layer", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public FolderDetailsResponse details(int folderId) {
        return mFolderService.getFolderDetail(folderId);
    }

    @Override
    public List<FolderPermissionsResponse> permissions(int folderId) {
        return mFolderService.getFolderPermission(folderId);
    }


    //region Helpers
    protected boolean AuthorizedToRename(int id) {
        Folder f = mFolderRepo.getById(id);

        return !(f.getIsFixed() || f.getIsImportFolder());
    }
    //endregion
}
