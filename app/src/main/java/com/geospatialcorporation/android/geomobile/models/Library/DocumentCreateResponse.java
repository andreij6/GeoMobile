package com.geospatialcorporation.android.geomobile.models.Library;

/**
 * Created by andre on 6/2/2015.
 */
public class DocumentCreateResponse {
    //region Getters & Setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }
    //endregion

    String Name;
    String Ext;
    String MimeType;
    long Size;
    String UploadTime;
}
