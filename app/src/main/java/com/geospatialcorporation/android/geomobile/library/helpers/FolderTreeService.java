package com.geospatialcorporation.android.geomobile.library.helpers;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FolderTreeService {

    private FolderService mFolderService;

    public FolderTreeService(){
        mFolderService = application.getRestAdapter().create(FolderService.class);
    }

    public Folder getFolderById(Integer folderId) {
        Folder folder = application.getFolderById(folderId);

        if(folder == null) {
            folder = mFolderService.getFolderById(folderId);
        }

        return folder;
    }

    public void createFolder(String name, int parentFolder){
        FolderCreateRequest createRequest = new FolderCreateRequest(name, parentFolder);


            mFolderService.createFolder(createRequest, new Callback<FolderCreateResponse>() {
            @Override
            public void success(FolderCreateResponse cr, Response response) {
                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull to Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });

    }

    public List<Folder> getFoldersByFolder(Integer folderId, boolean checkCache) {
        Folder folder = application.getFolderById(folderId);
        List<Folder> result;

        if(folder != null && folder.getFolders().size() > 0 && checkCache){
            result = folder.getFolders();
        } else {
            result = mFolderService.getFoldersByFolder(folderId);

            application.setFolders(result);
        }

        return result;
    }

    public List<Layer> getLayersByFolder(Integer folderId, Boolean checkCache) {
        Folder folder = application.getFolderById(folderId);
        List<Layer> result;

        if(folder != null && folder.getLayers().size() > 0 && checkCache){
            result = folder.getLayers();

        } else {
            result = mFolderService.getLayersByFolder(folderId);

            application.setLayers(result);

        }

        return result;
    }

    public List<Document> getDocumentsByFolder(Integer folderId){
        Folder folder = application.getFolderById(folderId);
        List<Document> result;

        if(folder != null && folder.getDocuments().size() > 0){
            result = folder.getDocuments();
        } else {
            result = mFolderService.getDocumentsByFolder(folderId);
        }

        return result;
    }

    public void deleteFolder(Folder folder) {
        mFolderService.delete(folder.getId(), new Callback<Folder>() {
            @Override
            public void success(Folder folder, Response response) {
                Toast.makeText(application.getAppContext(), "Success", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull to Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });

        application.removeFolder(folder.getId());
    }
}
