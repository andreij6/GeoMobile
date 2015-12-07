package com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery;

import com.geospatialcorporation.android.geomobile.models.Layers.Point;

import java.util.List;

public class Ring {
    //region Gs & Ss
    public Integer getGeometryTypeCode() {
        return GeometryTypeCode;
    }

    public void setGeometryTypeCode(Integer geometryTypeCode) {
        GeometryTypeCode = geometryTypeCode;
    }

    public List<Point> getPoints() {
        return Points;
    }

    public void setPoints(List<Point> points) {
        Points = points;
    }

    public int getPointCount() {
        return PointCount;
    }

    public void setPointCount(int pointCount) {
        PointCount = pointCount;
    }

    //endregion

    Integer GeometryTypeCode;
    List<Point> Points;
    int PointCount;
}
