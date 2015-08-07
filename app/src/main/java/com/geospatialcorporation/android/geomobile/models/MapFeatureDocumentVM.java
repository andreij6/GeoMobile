package com.geospatialcorporation.android.geomobile.models;

import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;

public class MapFeatureDocumentVM {
    private Integer mId;
    private Integer mSize;
    private String mName;

    public MapFeatureDocumentVM(String nameWithExt, Integer fileSize, Integer id) {
        mId = id;
        mSize = fileSize;
        mName = nameWithExt;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Integer getSize() {
        return mSize;
    }

    public String getName() {
        return mName;
    }

    public String getFormattedSize() {
        return FileSizeFormatter.format(getSize() + "");
    }
}
