package com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery;


/**
 * Created by andre on 6/24/2015.
 */
public class Feature {
    String Id;
    Geometry Shape;

    //region Getters & Setters
    public Geometry getGeometry() {
        return Shape;
    }

    public void setGeometry(Geometry geom) {
        Shape = geom;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
    //endregion

}
