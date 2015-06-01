package com.geospatialcorporation.android.geomobile.ui.viewmodels;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public int getIconId() {
        return IconId;
    }

    public void setIconId(int iconId) {
        IconId = iconId;
    }
    //endregion

    private String Name;
    private int Id;
    private int Order;



    private int IconId;

    //region Constructors
    public ListItem(Folder folder) {
        Name = folder.getName();
        Id = folder.getId();
        Order = 1;
        IconId = R.drawable.ic_folder_black_24dp;
    }

    public ListItem(Layer layer) {
        Name = layer.getName();
        Id = layer.getId();
        Order = 2;
        IconId = R.drawable.ic_layers_black_24dp;
    }

    public ListItem(Document document) {
        Name = document.getNameWithExt();
        Id = document.getId();
        Order = 3;
        IconId = setIconIdFromExt(document.getExtension());
    }

    public ListItem() {
    }
    //endregion

    @Override
    public int compareTo(ListItem another) {
        Boolean ordered = Order > another.getOrder();

        return ordered ? 0 : 1;
    }

    private int setIconIdFromExt(String documentExt) {
        List<String> word = Arrays.asList(new String[]{".docx", ".docm", ".dotx", ".dotm", ".docb"});
        List<String> excel = Arrays.asList(new String[]{".xls", ".xlt", ".xlm", ".xlsx", ".xltx", ".xltm", ".xlsb", ".xla", ".xlam", ".xll", ".xlw"});
        List<String> powerpoint = Arrays.asList(new String[] { ".ppt", ".pot", ".pps", ".pptx"});
        List<String> images = Arrays.asList(new String[] { ".jpg", ".png", ".jpeg"});

        String lowerExt = documentExt.toLowerCase();

        if(word.contains(lowerExt)){
            return R.drawable.ic_file_word_black_24dp;
        }

        if(excel.contains(lowerExt)){
            return R.drawable.ic_file_excel_black_24dp;
        }

        if(powerpoint.contains(lowerExt)){
            return R.drawable.ic_file_powerpoint_black_24dp;
        }

        if(images.contains(lowerExt)){
            return R.drawable.ic_file_image_black_24dp;
        }

        return R.drawable.ic_file_document_box_black_24dp;
    }
}
