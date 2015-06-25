package com.geospatialcorporation.android.geomobile.models.Query.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class Parameters {
    List<Integer> LayerIds;
    List<Layers> Layers;

    //region Getters & Setters
    public List<Layers> getLayers() {
        return Layers;
    }

    public void setLayers(List<Layers> layers) {
        Layers = layers;
    }

    public List<Integer> getLayerIds() {
        return LayerIds;
    }

    public void setLayerIds(List<Integer> layerIds) {
        LayerIds = layerIds;
    }
    //endregion

    public Parameters(List<Layers> layers){
        Layers = layers;
        LayerIds = new ArrayList<>(); //I could get layerIds but Dev doesnt have anything here for the layer using to build this
    }
}