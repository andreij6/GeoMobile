package com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery;

import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.ShapeModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Geometry extends ShapeModel {


    double X;
    double Y;
    double Z;
    double M;
    List<Ring> Rings;
    List<Geometry> Points;
    List<Geometry> Polygons;
    List<Geometry> Lines;
    int PointCount;
    int PointOrder;

    public Geometry(LatLng ll) {
        setGeometryTypeCode(1);
        setX(ll.longitude);
        setY(ll.latitude);
    }

    public Geometry(){

    }

    //region Getters & Setters

    public int getPointOrder() {
        return PointOrder;
    }

    public void setPointOrder(int pointOrder) {
        PointOrder = pointOrder;
    }

    public int getPointCount() {
        return PointCount;
    }

    public void setPointCount(int pointCount) {
        PointCount = pointCount;
    }

    public List<Geometry> getLines() {
        return Lines;
    }

    public void setLines(List<Geometry> lines) {
        Lines = lines;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }

    public List<Geometry> getPoints() {
        return Points;
    }

    public void setPoints(List<Geometry> points) {
        Points = points;
    }

    public LatLng getLatLng() {
        return new LatLng(getY(), getX());
    }

    public List<Ring> getRings() {
        return Rings;
    }

    public void setRings(List<Ring> rings) {
        Rings = rings;
    }

    public List<Geometry> getPolygons() {
        return Polygons;
    }

    public void setPolygons(List<Geometry> polygons) {
        Polygons = polygons;
    }
    //endregion
}
