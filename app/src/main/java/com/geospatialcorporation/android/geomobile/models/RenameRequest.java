package com.geospatialcorporation.android.geomobile.models;

/**
 * Created by andre on 6/3/2015.
 */
public class RenameRequest {

    public RenameRequest(String newName){
        Name = newName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;

}
