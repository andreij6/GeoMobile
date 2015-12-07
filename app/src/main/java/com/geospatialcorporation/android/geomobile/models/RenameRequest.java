package com.geospatialcorporation.android.geomobile.models;


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
