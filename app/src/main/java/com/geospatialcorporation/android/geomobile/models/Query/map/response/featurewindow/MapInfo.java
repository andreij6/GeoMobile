package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;

import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;

/**
 * Created by andre on 7/2/2015.
 */
public class MapInfo {
    String GeometryType;
    Point Point;

    Point EndPoint;
    Extent Extent;
    double Length;
    Integer PointCount;
    Point StartPoint;

    Point CenterPoint;
    double Area;

    //Multi*
    Integer PolygonCount;
    Integer LineCount;

    //region Gs & Ss
    public String getGeometryType() {
        return GeometryType;
    }

    public void setGeometryType(String geometryType) {
        GeometryType = geometryType;
    }

    public com.geospatialcorporation.android.geomobile.models.Layers.Point getPoint() {
        return Point;
    }

    public void setPoint(com.geospatialcorporation.android.geomobile.models.Layers.Point point) {
        Point = point;
    }

    public Point getEndPoint() {
        return EndPoint;
    }

    public void setEndPoint(Point endPoint) {
        EndPoint = endPoint;
    }

    public Extent getExtent() {
        return Extent;
    }

    public void setExtent(Extent extent) {
        Extent = extent;
    }

    public double getLength() {
        return Length;
    }

    public void setLength(double length) {
        Length = length;
    }

    public Integer getPointCount() {
        return PointCount;
    }

    public void setPointCount(Integer pointCount) {
        PointCount = pointCount;
    }

    public Point getStartPoint() {
        return StartPoint;
    }

    public void setStartPoint(Point startPoint) {
        StartPoint = startPoint;
    }

    public Point getCenterPoint() {
        return CenterPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        CenterPoint = centerPoint;
    }

    public double getArea() {
        return Area;
    }

    public void setArea(double area) {
        Area = area;
    }

    public Integer getPolygonCount() {
        return PolygonCount;
    }

    public void setPolygonCount(Integer polygonCount) {
        PolygonCount = polygonCount;
    }

    public Integer getLineCount() {
        return LineCount;
    }

    public void setLineCount(Integer lineCount) {
        LineCount = lineCount;
    }
    //endregion
}
