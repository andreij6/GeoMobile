package com.geospatialcorporation.android.geomobile.models.Query.map.response;

import com.geospatialcorporation.android.geomobile.models.Layers.Point;

import java.util.List;

/**
 * Created by andre on 6/25/2015.
 */
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
    //endregion

    Integer GeometryTypeCode;
    List<Point> Points;
}
