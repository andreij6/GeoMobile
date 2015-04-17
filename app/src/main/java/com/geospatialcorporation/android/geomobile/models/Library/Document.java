package com.geospatialcorporation.android.geomobile.models.Library;

/**
 * Created by andre on 4/7/2015.
 */
public class Document {

    //region Properties
    public int Id;
    public String Type;
    public Boolean IsDownloaded;
    //endregion

    public Document(String type, Boolean isDownloaded){
        Type = type;
        IsDownloaded = isDownloaded;
    }

    public Document(){}
}
