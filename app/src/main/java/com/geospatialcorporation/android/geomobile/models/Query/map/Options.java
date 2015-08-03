package com.geospatialcorporation.android.geomobile.models.Query.map;

/**
 * Created by andre on 6/24/2015.
 */
public class Options {

    public static final String FEATURE_WINDOW = "feature window";
    public static final String MAP_QUERY = "map query";

    public Options(String optionSetup){
        if(optionSetup.equals(MAP_QUERY)) {
            setMapQueryOptions();
        } else {
            setFeatureWindowOptions();
        }
    }

    private void setFeatureWindowOptions() {
        Attributes = true;
        Columns = true;
        Documents = true;
        MapInfo = true;
        Sublayers = true;
    }

    Boolean Attributes;
    Boolean Columns;
    Boolean Documents;
    Boolean MapInfo;
    //Sublayers

    private void setMapQueryOptions() {
        ExcludeTransparent = true;
        FeatureVectors = true;
        Style = true;
        Sublayers = true;
    }

    Boolean ExcludeTransparent;
    Boolean FeatureVectors;
    Boolean Style;
    Boolean Sublayers;

    //region Getters & Setters
    public Boolean getSublayers() {
        return Sublayers;
    }

    public void setSublayers(Boolean sublayers) {
        Sublayers = sublayers;
    }

    public Boolean getExcludeTransparent() {
        return ExcludeTransparent;
    }

    public void setExcludeTransparent(Boolean excludeTransparent) {
        ExcludeTransparent = excludeTransparent;
    }

    public Boolean getFeatureVectors() {
        return FeatureVectors;
    }

    public void setFeatureVectors(Boolean featureVectors) {
        FeatureVectors = featureVectors;
    }

    public Boolean getStyle() {
        return Style;
    }

    public void setStyle(Boolean style) {
        Style = style;
    }

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

    //endregion
}