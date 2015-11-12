package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import com.geospatialcorporation.android.geomobile.models.Layers.Extent;

import java.util.HashMap;
import java.util.List;

public class LayerFeatureChangeResponse {
    Extent newLayerExtent;
    List<FeatureChangeResult> Result;
    HashMap<String, Integer> CreatedMapFeatureIds;
    Boolean TypeCodeChanged;
    int NewGeomType;

    //region G's & S's
    public Extent getNewLayerExtent() {
        return newLayerExtent;
    }

    public void setNewLayerExtent(Extent newLayerExtent) {
        this.newLayerExtent = newLayerExtent;
    }

    public List<FeatureChangeResult> getResult() {
        return Result;
    }

    public void setResult(List<FeatureChangeResult> result) {
        Result = result;
    }

    public HashMap<String, Integer> getCreatedMapFeatureIds() {
        return CreatedMapFeatureIds;
    }

    public void setCreatedMapFeatureIds(HashMap<String, Integer> createdMapFeatureIds) {
        CreatedMapFeatureIds = createdMapFeatureIds;
    }

    public Boolean getTypeCodeChanged() {
        return TypeCodeChanged;
    }

    public void setTypeCodeChanged(Boolean typeCodeChanged) {
        TypeCodeChanged = typeCodeChanged;
    }

    public int getNewGeomType() {
        return NewGeomType;
    }

    public void setNewGeomType(int newGeomType) {
        NewGeomType = newGeomType;
    }
    //endregion
}
