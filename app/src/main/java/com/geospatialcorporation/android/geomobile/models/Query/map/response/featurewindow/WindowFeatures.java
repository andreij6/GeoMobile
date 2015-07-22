package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;

import java.util.List;

/**
 * Created by andre on 7/2/2015.
 */
public class WindowFeatures {
    List<String> Attributes;
    Integer Id;
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

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
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
