package com.geospatialcorporation.android.geomobile.models.Query;


public class Options {
    Boolean Attributes;
    Boolean Columns;
    Boolean Documents;
    Boolean MapInfo;
    Boolean Sublayers;

    //region Getters and Setters
    public Boolean getAttributes() {
        return Attributes;
    }

    public void setAttributes(Boolean attributes) {
        Attributes = attributes;
    }

    public Boolean getColumns() {
        return Columns;
    }

    public void setColumns(Boolean columns) {
        Columns = columns;
    }

    public Boolean getDocuments() {
        return Documents;
    }

    public void setDocuments(Boolean documents) {
        Documents = documents;
    }

    public Boolean getMapInfo() {
        return MapInfo;
    }

    public void setMapInfo(Boolean mapInfo) {
        MapInfo = mapInfo;
    }

    public Boolean getSublayers() {
        return Sublayers;
    }

    public void setSublayers(Boolean sublayers) {
        Sublayers = sublayers;
    }
    //endregion

    public Options(){
        Attributes = true;
        Columns = true;
        Documents = true;
        MapInfo = true;
        Sublayers = true;
    }
}
