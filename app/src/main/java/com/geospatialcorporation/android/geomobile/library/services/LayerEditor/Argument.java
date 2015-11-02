package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;

public class Argument {
    int TypeCode;
    Geometry MapFeature;
    String MapFeatureId;
    Geometry OldFeature;
    int LayerId;

    //region G's & S's
    public int getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(int typeCode) {
        TypeCode = typeCode;
    }

    public Geometry getMapFeature() {
        return MapFeature;
    }

    public void setMapFeature(Geometry mapFeature) {
        MapFeature = mapFeature;
    }

    public String getMapFeatureId() {
        return MapFeatureId;
    }

    public void setMapFeatureId(String mapFeatureId) {
        MapFeatureId = mapFeatureId;
    }

    public Geometry getOldFeature() {
        return OldFeature;
    }

    public void setOldFeature(Geometry oldFeature) {
        OldFeature = oldFeature;
    }

    public int getLayerId() {
        return LayerId;
    }

    public void setLayerId(int layerId) {
        LayerId = layerId;
    }

    //endregion
}
