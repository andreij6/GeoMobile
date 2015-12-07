package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;

public class MapFeatureFiles {
    Integer Id;
    String Name;
    String Ext;
    String MimeType;
    String UploadTime;
    Integer Size;

    //region Gs & Ss
    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

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

    public void setSize(Integer size){
        Size = size;
    }

    public Integer getSize(){
        return Size;
    }
    //endregion

    //region Methods
    public String nameWithExt(){
        return Name + Ext;
    }
    //endregion


}
