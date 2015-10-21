package com.geospatialcorporation.android.geomobile.models;

import java.util.List;

public class PluginSubscriptionResponse {
    List<Subscription> Items;
    int Count;

    public List<Subscription> getItems() {
        return Items;
    }

    public void setItems(List<Subscription> items) {
        Items = items;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
