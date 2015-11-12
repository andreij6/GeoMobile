package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;

import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;

import java.io.Serializable;
import java.util.List;


public class FeatureQueryResponse implements Serializable {
    List<Columns> Columns;  //Id Name and DataType
    Integer Id;
    List<MapQueryFeatures> Sublayers;
    List<WindowFeatures> Features;

    String VideoHashId;
    VideoDetailsModel VideoDetails;
    Geometry Line;

    //region Gs & Ss
    public List<com.geospatialcorporation.android.geomobile.models.Layers.Columns> getColumns() {
        return Columns;
    }

    public void setColumns(List<com.geospatialcorporation.android.geomobile.models.Layers.Columns> columns) {
        Columns = columns;
    }

    public String getVideoHashId() {
        return VideoHashId;
    }

    public void setVideoHashId(String videoHashId) {
        VideoHashId = videoHashId;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public List<MapQueryFeatures> getSublayers() {
        return Sublayers;
    }

    public void setSublayers(List<MapQueryFeatures> sublayers) {
        Sublayers = sublayers;
    }

    public List<WindowFeatures> getFeatures() {
        return Features;
    }

    public void setFeatures(List<WindowFeatures> features) {
        Features = features;
    }
    //endregion
}
