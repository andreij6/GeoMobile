package com.geospatialcorporation.android.geomobile.library.DI.Map.Models;

import com.google.android.gms.maps.GoogleMap;

public class BookmarkMapStateSaveRequest extends MapStateSaveRequest {

    public BookmarkMapStateSaveRequest(GoogleMap map, String name) {
        super(map);
        mName = name;
    }

    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
