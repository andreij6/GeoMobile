package com.geospatialcorporation.android.geomobile.library.services;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.FolderModifiedListener;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.List;

import retrofit.client.Response;

public class FolderTreeService implements ITreeService {
    private static final String TAG = FolderTreeService.class.getSimpleName();

    //region Properties
    private FolderService mFolderService;
    FolderAppSource FolderRepo;
    IFullDataRepository<Layer> LayerRepo;
    private TreeService mTreeService;
    //endregion

    //region Constructors
    public FolderTreeService(){
        mFolderService = application.getRestAdapter().create(FolderService.class);
        FolderRepo = new FolderAppSource();
        LayerRepo = new LayersAppSource();
        mTreeService = application.getRestAdapter().create(TreeService.class);
    }
    //endregion

    //region Public Methods
    public Folder getFolderById(Integer folderId) {
        Folder folder = FolderRepo.getById(folderId);

        if(folder == null) {
            folder = mFolderService.getFolderById(folderId);
        }

        return folder;
    }

    public List<Folder> getFoldersByFolder(Integer folderId, boolean checkCache) {
        Folder folder = FolderRepo.getById(folderId);
        List<Folder> result;

        if(folder != null && folder.getFolders().size() > 0 && checkCache){
            result = folder.getFolders();
        } else {
            result = mFolderService.getFoldersByFolder(folderId);

            FolderRepo.Add(result);
        }

        return result;
    }

    public List<Layer> getLayersByFolder(Integer folderId, Boolean checkCache) {

        return mFolderService.getLayersByFolder(folderId);

    }

    public List<Document> getDocumentsByFolder(Integer folderId){
        return mFolderService.getDocumentsByFolder(folderId);

    }

    public void createFolder(String name, int parentFolder){
        FolderCreateRequest createRequest = new FolderCreateRequest(name, parentFolder);

        mFolderService.createFolder(createRequest, new RequestCallback<>(new FolderModifiedListener()));
    }

    public void deleteFolder(Folder folder) {
        mFolderService.remove(folder.getId(), new RequestCallback<>(new FolderModifiedListener()));
    }

    public void rename(final int folderId,final String folderName){

        if(AuthorizedToRename(folderId)) {

            mFolderService.rename(folderId, new RenameRequest(folderName), new RequestCallback<>(new FolderModifiedListener()));
        } else {
            Toast.makeText(application.getAppContext(), "Not Authorized to Rename Layer", Toast.LENGTH_LONG).show();
        }

    }

    public void delete(Folder folder) {
        mFolderService.remove(folder.getId(), new RequestCallback<Response>(new FolderModifiedListener()));
    }

    public FolderDetailsResponse details(int folderId){
        return mFolderService.getFolderDetail(folderId);
    }

    public List<FolderPermissionsResponse> permissions(int folderId){
        return mFolderService.getFolderPermission(folderId);
    }
    //endregion

    //region Helpers
    protected boolean AuthorizedToRename(int id) {
        Folder f = FolderRepo.getById(id);

        if(f.getIsFixed() || f.getIsImportFolder()){
            return false;
        }

        return true;
    }
    //endregion


}
