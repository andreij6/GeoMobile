package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.point.Point;
import com.google.android.gms.maps.model.Polygon;

public class Change {
    int ActionId;
    int OldGeometryTypeCode;
    ShapeModel OldFeature;
    Argument Arguments;

    //region G's & S's

    public int getActionId() {
        return ActionId;
    }

    public void setActionId(int actionId) {
        ActionId = actionId;
    }

    public int getOldGeometryTypeCode() {
        return OldGeometryTypeCode;
    }

    public void setOldGeometryTypeCode(int oldGeometryTypeCode) {
        OldGeometryTypeCode = oldGeometryTypeCode;
    }

    public ShapeModel getOldFeature() {
        return OldFeature;
    }

    public void setOldFeature(ShapeModel oldFeature) {
        OldFeature = oldFeature;
    }

    public Argument getArguments() {
        return Arguments;
    }

    public void setArguments(Argument arguments) {
        Arguments = arguments;
    }

    //endregion

    public static final int ADD = 1;
    public static final int REMOVE = 0;
    public static final int MOVE = 2;

    public void updateMapFeature(Geometry point) {
        Arguments.setMapFeature(point);
    }

    public static class Factory {
        public Change Create(int action, int layerId, ShapeModel point, ShapeModel oldFeature, String mapFeatureId, int geomTypeCode){
            if(action == ADD){
                mapFeatureId = "-1";
            }

            Change result = new Change();
            result.setActionId(action);
            result.setOldFeature(oldFeature);
            result.setOldGeometryTypeCode(geomTypeCode);

            Argument arg = new Argument();
            arg.setTypeCode(geomTypeCode);
            arg.setMapFeature(point);
            arg.setMapFeatureId(mapFeatureId);
            arg.setOldFeature(oldFeature);
            arg.setLayerId(layerId);

            result.setArguments(arg);

            return result;
        }
    }
}
