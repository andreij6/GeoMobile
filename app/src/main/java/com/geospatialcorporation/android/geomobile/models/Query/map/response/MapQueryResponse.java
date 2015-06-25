package com.geospatialcorporation.android.geomobile.models.Query.map.response;

import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class MapQueryResponse {
    //region Getters & Setters
    public List<com.geospatialcorporation.android.geomobile.models.Query.map.response.Sublayers> getSublayers() {
        return Sublayers;
    }

    public void setSublayers(List<com.geospatialcorporation.android.geomobile.models.Query.map.response.Sublayers> sublayers) {
        Sublayers = sublayers;
    }

    public List<Feature> getFeatures() {
        return Features;
    }

    public void setFeatures(List<Feature> features) {
        Features = features;
    }

    public Style getStyle() {
        return Style;
    }

    public void setStyle(Style style) {
        Style = style;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    //endregion

    List<Feature> Features;
    Style Style;
    List<Sublayers> Sublayers;
    Integer Id;

}
