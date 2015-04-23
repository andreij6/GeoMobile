package com.geospatialcorporation.android.geomobile.models.Folders;


import android.os.Parcel;
import android.os.Parcelable;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

public class Folder implements Parcelable{

    public Folder(){
        Layers = new ArrayList<>();
        Folders = new ArrayList<>();
    }

    //region Properties
    private Boolean IsImportFolder;
    private Boolean IsFixed;
    private List<Layer> Layers;
    private List<Folder> Folders;
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

    //region Constants
    public static final String FOLDER_INTENT = "Folder";
    public static final String LAYER = "Layer";
    public static final String LIBRARY = "Library";
    //endregion

    //region Parcelable Contract
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(IsImportFolder);
        dest.writeValue(IsFixed);
        dest.writeTypedList(Layers);
        dest.writeTypedList(Folders);
        dest.writeInt(AccessLevel);
        dest.writeInt(Id);
        dest.writeString(Name);
    }

    private Folder(Parcel in){
        IsImportFolder = (Boolean)in.readValue(Boolean.class.getClassLoader());
        IsFixed = (Boolean)in.readValue(Boolean.class.getClassLoader());
        Layers = new ArrayList<>();
        in.readTypedList(Layers, Layer.CREATOR);
        Folders = new ArrayList<>();
        in.readTypedList(Folders, Folder.CREATOR);
        AccessLevel = in.readInt();
        Id = in.readInt();
        Name = in.readString();

    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>(){

        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    //endregion


}
