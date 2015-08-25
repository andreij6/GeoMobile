package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces;

import android.net.Uri;

import com.geospatialcorporation.android.geomobile.library.ISendFileCallback;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

public interface IDocumentTreeService {
    void delete(int documentId);

    void sendDocument(Folder currentFolder, Uri data, ISendFileCallback callback);

    void sendTakenImage(Folder currentFolder, Uri data, ISendFileCallback callback);

    void sendPickedImage(Folder currentFolder, Uri data, ISendFileCallback callback);

    void download(int documentId, String documentName);

    void rename(int documentId, String documentName);

    void move(Document document, int folderId);
}
