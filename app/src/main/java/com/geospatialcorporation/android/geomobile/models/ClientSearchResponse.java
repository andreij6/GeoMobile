package com.geospatialcorporation.android.geomobile.models;

import java.util.List;

public class ClientSearchResponse {
    int Count;
    List<Subscription> Items;

    //region G's & S's
    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public List<Subscription> getItems() {
        return Items;
    }

    public void setItems(List<Subscription> items) {
        Items = items;
    }
    //endregion
}
