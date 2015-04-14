package com.geospatialcorporation.android.geomobile.models.LibraryItems;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryItem {

    //region Properties
    private String Type;
    private Boolean IsDownloaded;
    //endregion

    public LibraryItem(String type, Boolean isDownloaded){
        Type = type;
        IsDownloaded = isDownloaded;
    }

    public LibraryItem(){}
}
