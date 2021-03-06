package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.FolderModifiedListener;
import com.geospatialcorporation.android.geomobile.api.FolderService;
import com.geospatialcorporation.android.geomobile.api.TreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

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
        if(AuthorizedToRename(folderId)) {
            mFolderService.remove(folderId, new RequestCallback<>(new FolderModifiedListener()));

            mFolderRepo.Remove(folderId);
        } else {
            String message = application.getAppContext().getString(R.string.not_authorized_to_delete_folder);

            Toast.makeText(application.getAppContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void rename(int folderId, String name) {
        if(AuthorizedToRename(folderId)) {

            mFolderService.rename(folderId, new RenameRequest(name), new RequestCallback<>(new FolderModifiedListener()));

            Folder folder = mFolderRepo.getById(folderId);
            folder.setName(name);

            mFolderRepo.update(folder, folderId);
        } else {
            String message = application.getAppContext().getString(R.string.not_authorized_to_rename_folder);

            Toast.makeText(application.getAppContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public FolderDetailsResponse details(int folderId) {
        return mFolderService.getFolderDetail(folderId);
    }

    @Override
    public List<FolderPermissionsResponse> permissions(int folderId) {
        List<FolderPermissionsResponse> result = new ArrayList<>();

        try {
            result = mFolderService.getFolderPermission(folderId);
        } catch (RetrofitError error){


        } finally {
            return  result;
        }
    }

    @Override
    public Folder getParentFolderByLayerId(Integer id) {
        List<Folder> folders =  mFolderRepo.getAll();

        Folder result = null;

        for (Folder folder : folders) {
            if(folder.getLayers() != null){
                for (Layer layer : folder.getLayers()) {
                    if(layer.getId() == id){
                        result = folder;
                        break;
                    }
                }
            }
        }

        return result;
    }


    //region Helpers
    protected boolean AuthorizedToRename(int id) {
        Folder f = mFolderRepo.getById(id);

        if(f == null){
            return false;
        }

        return !(f.getIsFixed() || f.getIsImportFolder() || !f.isEditable());
    }
    //endregion
}
