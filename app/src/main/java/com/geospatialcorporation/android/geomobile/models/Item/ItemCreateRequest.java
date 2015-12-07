package com.geospatialcorporation.android.geomobile.models.Item;

public class ItemCreateRequest {
    //region Getters & Setters
    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion

    protected int ParentId;
    protected String Name;
}
