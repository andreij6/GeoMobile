package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

public class FeatureChangeResult {
    int MapFeatureId;
    Boolean Successful;
    Boolean GeometryChanged;

    //region G's & S's
    public int getMapFeatureId() {
        return MapFeatureId;
    }

    public void setMapFeatureId(int mapFeatureId) {
        MapFeatureId = mapFeatureId;
    }

    public Boolean getSuccessful() {
        return Successful;
    }

    public void setSuccessful(Boolean successful) {
        Successful = successful;
    }

    public Boolean getGeometryChanged() {
        return GeometryChanged;
    }

    public void setGeometryChanged(Boolean geometryChanged) {
        GeometryChanged = geometryChanged;
    }
    //endregion
}
