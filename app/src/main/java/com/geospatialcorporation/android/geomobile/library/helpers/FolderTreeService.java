package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.FolderService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

/**
 * Created by andre on 5/29/2015.
 */
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

    public List<Folder> getFoldersByFolder(Integer folderId) {
        Folder folder = application.getFolderById(folderId);
        List<Folder> result;

        if(folder != null && folder.getFolders().size() > 0){
            result = folder.getFolders();
        } else {
            result = mFolderService.getFoldersByFolder(folderId);
        }

        return result;
    }

    public List<Layer> getLayersByFolder(Integer folderId) {
        Folder folder = application.getFolderById(folderId);
        List<Layer> result;

        if(folder != null && folder.getLayers().size() > 0){
            result = folder.getLayers();

        } else {
            result = mFolderService.getLayersByFolder(folderId);
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
}
