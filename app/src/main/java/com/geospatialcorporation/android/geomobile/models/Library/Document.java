package com.geospatialcorporation.android.geomobile.models.Library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }
    //endregion

    //region Properties
    private int DocumentId;
    private long Size;
    private String Name;
    private String UploadTime;
    private String MimeType;
    private String Extension;
    //endregion

    public Date getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy h:mm a");

        Date date = null;

        try {
            date = formatter.parse(UploadTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public Document(){}
}
