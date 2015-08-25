package com.geospatialcorporation.android.geomobile.models;

public class Subscription {
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

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    //endregion

    String Name;
    int Id;
    int Type;
    String Created;

    public Subscription(String name, int id, int type, String created) {
        Name = name;
        Id = id;
        Type = type;
        Created = created;
    }

}
