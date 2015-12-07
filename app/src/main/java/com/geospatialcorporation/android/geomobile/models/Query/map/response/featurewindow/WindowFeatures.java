package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;

import java.util.List;

public class WindowFeatures {
    List<String> Attributes;
    String Id;
    MapInfo MapInfo;
    List<MapFeatureFiles> Files;

    //region Gs & Ss
    public MapInfo getMapInfo() {
        return MapInfo;
    }

    public void setMapInfo(MapInfo mapInfo) {
        MapInfo = mapInfo;
    }

    public List<String> getAttributes() {
        return Attributes;
    }

    public void setAttributes(List<String> attributes) {
        Attributes = attributes;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setFiles(List<MapFeatureFiles> files){
        Files = files;
    }

    public List<MapFeatureFiles> getFiles(){
        return Files;
    }
    //endregion

}
