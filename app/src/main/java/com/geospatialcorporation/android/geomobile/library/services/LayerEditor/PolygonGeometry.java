package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Ring;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class PolygonGeometry extends ShapeModel {
    List<Ring> Rings;
    int PointOrder;

    public PolygonGeometry(Polygon polygon){
        GeometryTypeCode = GeometryTypeCodes.Polygon;
        Rings = getRingsByPolygon(polygon);
        PointOrder = getPointOrder(polygon.getPoints());
    }

    private int getPointOrder(List<LatLng> points) {
        return 2;
    }

    private List<Ring> getRingsByPolygon(Polygon polygon) {
        Ring ring = new Ring();
        ring.setGeometryTypeCode(GeometryTypeCodes.Line);

        List<Point> points = new ArrayList<>();

        for (LatLng point : polygon.getPoints()) {
            points.add(new Point(point));
        }

        ring.setPoints(points);
        ring.setPointCount(points.size() - 1);

        List<Ring> rings = new ArrayList<>();
        rings.add(ring);

        return rings;
    }

    public List<Ring> getRings() {
        return Rings;
    }

    public void setRings(List<Ring> rings) {
        Rings = rings;
    }

    public int getPointOrder() {
        return PointOrder;
    }

    public void setPointOrder(int pointOrder) {
        PointOrder = pointOrder;
    }
}
