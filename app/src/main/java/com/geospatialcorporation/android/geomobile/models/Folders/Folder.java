package com.geospatialcorporation.android.geomobile.models.Folders;


import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class Folder {
    //region Properties
    private Boolean mIsImportFolder;
    private Boolean mIsFixed;
    private ArrayList<Layer> mLayers;
    private ArrayList<Folder> mFolders;
    private Integer mAccessLevel;
    private Integer mId;
    private String mName;
    //endregion

    //region Getters & Setters
    public Boolean getIsImportFolder() {
        return mIsImportFolder;
    }

    public void setIsImportFolder(Boolean isImportFolder) {
        mIsImportFolder = isImportFolder;
    }

    public Boolean getIsFixed() {
        return mIsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        mIsFixed = isFixed;
    }

    public ArrayList<Layer> getLayers() {
        return mLayers;
    }

    public void setLayers(ArrayList<Layer> layers) {
        mLayers = layers;
    }

    public ArrayList<Folder> getFolders() {
        return mFolders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        mFolders = folders;
    }

    public Integer getAccessLevel() {
        return mAccessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        mAccessLevel = accessLevel;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
    //endregion
}
