package com.geospatialcorporation.android.geomobile.models;

/**
 * Created by andre on 6/5/2015.
 */
public class Bookmark {
    //region Getters & Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion

    int Id;
    String Name;
}
