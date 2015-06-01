package com.geospatialcorporation.android.geomobile.models;

import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;

public class Client {
    //region Getters & Setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
    //endregion

    String Name;
    int Id;
    int Type;

    public Client(String name, int id, int type) {
        Name = name;
        Id = id;
        Type = type;
    }

}
