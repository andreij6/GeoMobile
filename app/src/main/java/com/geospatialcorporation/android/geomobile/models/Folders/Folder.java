package com.geospatialcorporation.android.geomobile.models.Folders;


import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

/**
 * Created by andre on 4/7/2015.
 */
public class Folder {

    //region Properties
    private Boolean IsImportFolder;
    private Boolean IsFixed;
    private List<Layer> Layers;
    private List<Folder> Folders;
    private List<Document> Documents;
    private Integer AccessLevel;
    private Integer MobileId;
    private Integer Id;
    private String Name;
    //endregion

    //region Getters & Setters
    public Boolean getIsImportFolder() {
        return IsImportFolder;
    }

    public void setIsImportFolder(Boolean isImportFolder) {
        IsImportFolder = isImportFolder;
    }

    public Boolean getIsFixed() {
        return IsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        IsFixed = isFixed;
    }

    public List<Layer> getLayers() {
        return Layers;
    }

    public void setLayers(List<Layer> layers) {
        Layers = layers;
    }

    public List<Folder> getFolders() {
        return Folders;
    }

    public void setFolders(List<Folder> folders) {
        Folders = folders;
    }

    public Integer getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        AccessLevel = accessLevel;
    }

    public Integer getMobileId() {
        return MobileId;
    }

    public void setMobileId(Integer id) {
        MobileId = id;
    }

    public Integer getId() {return Id; }

    public void setId(Integer id){ Id = id; }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion


}
