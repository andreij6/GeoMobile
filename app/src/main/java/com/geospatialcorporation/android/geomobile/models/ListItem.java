package com.geospatialcorporation.android.geomobile.models;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;

public class ListItem implements Comparable<ListItem> {
    public static final int FOLDER = 1;
    public static final int LAYER = 2;
    public static final int DOCUMENT = 3;
    public static final int EMPTYLayer = 4;
    public static final int EMPTYDocument = 5;

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

    public int getIconId() {
        return IconId;
    }

    public void setIconId(int iconId) {
        IconId = iconId;
    }

    public Boolean getShowInfoIcon() {
        return showInfoIcon;
    }

    public void setShowInfoIcon(Boolean showInfoIcon) {
        this.showInfoIcon = showInfoIcon;
    }

    public Boolean getIsCompletelyEmpty() {
        return IsCompletelyEmpty;
    }

    public void setIsCompletelyEmpty(Boolean isCompletelyEmpty) {
        IsCompletelyEmpty = isCompletelyEmpty;
    }

    //endregion

    private String Name;
    private int Id;
    private int Order;
    private Boolean showInfoIcon;
    private Boolean IsCompletelyEmpty;



    private int IconId;

    //region Constructors
    public ListItem(Folder folder) {
        Name = folder.getName();
        Id = folder.getId();
        Order = 1;
        IconId = R.drawable.ic_folder_black_24dp;
        showInfoIcon = true;
        IsCompletelyEmpty = false;

    }

    public ListItem(Folder folder, Boolean isEmpty) {
        Name = "No Folders";
        Id = 0;
        Order = 0;
        IconId = 0;
        showInfoIcon = false;
        IsCompletelyEmpty = false; //only ListItem(int empty) is true

    }

    public ListItem(Layer layer) {
        Name = layer.getName();
        Id = layer.getId();
        Order = 2;
        IconId = R.drawable.ic_layers_black_24dp;
        showInfoIcon = true;
        IsCompletelyEmpty = false;

    }

    public ListItem(Layer layer, Boolean isEmpty) {
        Name = "No Layers";
        Id = 0;
        Order = EMPTYLayer;
        IconId = 0;
        showInfoIcon = false;
        IsCompletelyEmpty = false; //only ListItem(int empty) is true

    }

    public ListItem(Document document) {
        Name = document.getNameWithExt();
        Id = document.getId();
        Order = 3;
        IconId = document.getFileTypeDrawable(false);
        showInfoIcon = true;
        IsCompletelyEmpty = false;

    }

    public ListItem(Document document, Boolean isEmpty) {
        Name = "No Documents";
        Id = 0;
        Order = EMPTYDocument;
        IconId = 0;
        showInfoIcon = false;
        IsCompletelyEmpty = false;  //only ListItem(int empty) is true

    }

    public ListItem(int empty) {
        Name = "";
        Id = 0;
        Order = empty == EMPTYDocument ? EMPTYDocument : EMPTYLayer;
        IconId = 0;
        showInfoIcon = false;
        IsCompletelyEmpty = true;
    }
    //endregion

    @Override
    public int compareTo(ListItem another) {
        Boolean ordered = Order > another.getOrder();

        return ordered ? 0 : 1;
    }

}
