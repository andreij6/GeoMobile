package com.geospatialcorporation.android.geomobile.ui.viewmodels;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

public class ListItem implements Comparable<ListItem> {
    public static final int FOLDER = 1;
    public static final int LAYER = 2;
    public static final int DOCUMENT = 3;

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

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    //endregion

    private String Name;
    private int Id;
    private int Order;

    //region Constructors
    public ListItem(Folder folder) {
        Name = folder.getName();
        Id = folder.getId();
        Order = 1;
    }

    public ListItem(Layer layer) {
        Name = layer.getName();
        Id = layer.getId();
        Order = 2;
    }

    public ListItem(Document document) {
        Name = document.getName();
        Id = document.getId();
        Order = 3;
    }

    public ListItem() {
    }
    //endregion

    @Override
    public int compareTo(ListItem another) {
        Boolean ordered = Order > another.getOrder();

        return ordered ? 0 : 1;
    }
}
