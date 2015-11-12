package com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.VideoDetailsModel;

import java.util.List;

public class MapQueryResponse {
    //region Getters & Setters
    public List<com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Sublayers> getSublayers() {
        return Sublayers;
    }

    public void setSublayers(List<com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Sublayers> sublayers) {
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

    public String getVideoHashId() {
        return VideoHashId;
    }

    public void setVideoHashId(String videoHashId) {
        VideoHashId = videoHashId;
    }

    public VideoDetailsModel getVideoDetails() {
        return VideoDetails;
    }

    public void setVideoDetails(VideoDetailsModel videoDetails) {
        VideoDetails = videoDetails;
    }

    public Geometry getLine() {
        return Line;
    }

    public void setLine(Geometry line) {
        Line = line;
    }

    //endregion

    List<Feature> Features;
    Style Style;
    List<Sublayers> Sublayers;
    Integer Id;

    String VideoHashId;
    VideoDetailsModel VideoDetails;
    Geometry Line;
}
