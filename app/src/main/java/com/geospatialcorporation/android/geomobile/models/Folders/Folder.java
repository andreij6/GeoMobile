package com.geospatialcorporation.android.geomobile.models.Folders;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.geospatialcorporation.android.geomobile.library.constants.AccessLevelCodes;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Interfaces.ITreeEntity;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Parcelable, ITreeEntity {

    //region Constructor
    public Folder(){
        Documents = new ArrayList<>();
        Layers = new ArrayList<>();
        Folders = new ArrayList<>();
        mPath = new ArrayList<>();
        IsImportFolder = false;
        IsFixed = false;
        AccessLevel = AccessLevelCodes.FullControl;  //TODO: test - may want to start with readonly in production
    }
    //endregion

    //region Properties
    private Boolean IsImportFolder;     //
    private Boolean IsFixed;            //
    private List<Document> Documents;
    private List<Layer> Layers;
    private List<Folder> Folders;
    private Folder Parent;
    private Integer AccessLevel;       //
    private Integer Id;                //
    private String Name;              //
    private List<String> mPath;
    //endregion

    //region Getters & Setters
    public Boolean getIsImportFolder() { return IsImportFolder; }

    public void setIsImportFolder(Boolean isImportFolder) { IsImportFolder = isImportFolder; }

    public Boolean getIsFixed() {
        return IsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        IsFixed = isFixed;
    }

    public List<Document> getDocuments() { return Documents; }

    public void setDocuments(List<Document> documents) { Documents = documents; }

    public List<Layer> getLayers() {
        return Layers;
    }

    public void setLayers(List<Layer> layers) {
        Layers = layers;
    }

    public List<Folder> getFolders() { return Folders; }

    public void setFolders(List<Folder> folders) {
        Folders = folders;
        for (Folder folder : folders) {
            folder.setParent(this);
        }
    }

    public Folder getParent() { return Parent; }
    public void setParent(Folder parent) { Parent = parent; }

    public Integer getAccessLevel() {
        return AccessLevel;
    }
    public void setAccessLevel(Integer accessLevel) {
        AccessLevel = accessLevel;
    }

    public int getId() {return Id; }
    public void setId(Integer id){ Id = id; }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public void setPathItem(String folderName){
        mPath.add(folderName);
    }
    //endregion

    //region Constants
    public static final String FOLDER_INTENT = "Folder";
    public static final String FOLDER_TYPE_INTENT = "Folder Type";
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
        Documents = new ArrayList<>();
        in.readTypedList(Documents, Document.CREATOR);
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

    //region Methods
    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putParcelable(FOLDER_INTENT, this);
        return b;
    }

    public Boolean isRoot(){
        return Name.equals("/");
    }

    public String getPrettyPath() {

        StringBuilder path = new StringBuilder();
        int count = 1;
        int depth = mPath.size();

        List<String> reordered = new ArrayList<>();

        for (String folder: mPath) {
            reordered.add(folder.toUpperCase());
        }

        Collections.reverse(reordered);

        for (String folderName: reordered) {

            path.append(folderName);

            if(count != depth){
                path.append(" > ");
            }

            count++;
        }

        return path.toString();
    }

    public List<String> getPath() {
        if(mPath != null && !mPath.isEmpty()){
            if(!mPath.get(0).equals("ROOT")){
                Collections.reverse(mPath);
            }
        } else {
            setPath(getParentPath(Parent));
        }

        return mPath;
    }

    public void setPath(List<String> path) {
        mPath = path;
    }

    public String getProperName() {
        if(Name.equals("/")){
            return "ROOT";
        } else {
            return Name;
        }
    }
    //endregion

    protected List<String> getParentPath(Folder parent) {
        List<String> paths = new ArrayList<>();

        if(parent != null) {
            if (parent.getParent() != null) {
                paths.add(parent.getName());
                paths.addAll(getParentPath(parent.getParent()));
            } else {
                paths.add("ROOT");
            }
        }

        Collections.reverse(paths);

        return paths;
    }

    public boolean isEditable() {
        if(AccessLevel == AccessLevelCodes.NoAccess || AccessLevel == AccessLevelCodes.ReadOnly){
            return false;
        } else {
            return true;
        }
    }

}
