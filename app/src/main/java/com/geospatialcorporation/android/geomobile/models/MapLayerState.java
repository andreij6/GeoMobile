package com.geospatialcorporation.android.geomobile.models;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/26/2015.
 */


public class MapLayerState {

    List<Integer> LayerIds;

    public MapLayerState(){
        LayerIds = new ArrayList<>();
    }

    public Boolean shouldShow(int layerId){
        return LayerIds.indexOf(layerId) != -1;
    }

    public void addLayer(Layer layer){
        if(!LayerIds.contains(layer.getId())) {
            LayerIds.add(layer.getId());
        }
    }

    public void removeLayer(Layer layer){
        int index = LayerIds.indexOf(layer.getId());
        try {
            LayerIds.remove(index);
        } catch (Exception e){

        }
    }
}
