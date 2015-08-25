package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces;

import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

public interface IFolderTreeService {
    Folder getById(int id);

    List<Folder> getFoldersByFolder(int folderId, boolean checkCache);

    List<Layer> getLayersByFolder(int folderId, boolean checkCache);

    List<Document> getDocumentsByFolder(int folderId);

    void create(String name, int parentFolderId);

    void delete(int folderId);

    void rename(int folderId, String name);

    FolderDetailsResponse details(int folderId);

    List<FolderPermissionsResponse> permissions(int folderId);

}
