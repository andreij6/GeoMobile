package com.geospatialcorporation.android.geomobile.models.Query.map;

/**
 * Created by andre on 6/24/2015.
 */
public class Options {

    public Options(){
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
    //endregion
}