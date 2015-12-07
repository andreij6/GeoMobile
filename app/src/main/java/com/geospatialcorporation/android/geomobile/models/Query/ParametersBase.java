package com.geospatialcorporation.android.geomobile.models.Query;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

public abstract class ParametersBase {
    //region Getters & Setters
    public List<Layer> getLayers() {
        return Layers;
    }

    public void setLayers(List<Layer> layers) {
        Layers = layers;
    }

    public List<Integer> getLayerIds() {
        return LayerIds;
    }

    public void setLayerIds(List<Integer> layerIds) {
        LayerIds = layerIds;
    }
    //endregion

    List<Layer> Layers;
    List<Integer> LayerIds;
}
