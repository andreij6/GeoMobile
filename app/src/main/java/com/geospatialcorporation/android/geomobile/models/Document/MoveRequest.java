package com.geospatialcorporation.android.geomobile.models.Document;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

/**
 * Created by andre on 6/9/2015.
 */
public class MoveRequest {
    Integer DocumentId;
    String Ext;
    Integer FolderId;
    String Name;

    //region Getters & Setters
    public Integer getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(Integer documentId) {
        DocumentId = documentId;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
    }

    public Integer getFolderId() {
        return FolderId;
    }

    public void setFolderId(Integer folderId) {
        FolderId = folderId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion

    public MoveRequest(Document document, int toFolderId){
        DocumentId = document.getId();
        Ext = document.getExtension();
        Name = document.getName();
        FolderId = toFolderId;
    }
}
