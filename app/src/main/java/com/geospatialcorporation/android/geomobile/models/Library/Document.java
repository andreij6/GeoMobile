package com.geospatialcorporation.android.geomobile.models.Library;

/**
 * Created by andre on 4/7/2015.
 */
public class Document {

    //region Getters & Setters
    public int getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(int documentId) {
        DocumentId = documentId;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }
    //endregion

    //region Properties
    private int DocumentId;
    private long Size;
    private String Name;
    //private Date UploadTime;
    private String MimeType;
    private String Extension;
    //endregion

    public Document(){}
}
