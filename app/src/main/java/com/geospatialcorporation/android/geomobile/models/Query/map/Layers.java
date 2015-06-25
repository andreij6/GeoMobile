package com.geospatialcorporation.android.geomobile.models.Query.map;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class Layers{
    //region Getters & Setters
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public List<Integer> getSublayerIds() {
        return SublayerIds;
    }

    public void setSublayerIds(List<Integer> sublayerIds) {
        SublayerIds = sublayerIds;
    }
    //endregion

    Integer Id;
    List<Integer> SublayerIds;

    public Layers(Layer layer){
        Id = layer.getId();
        SublayerIds = getSublayerIds(layer);
    }

    private List<Integer> getSublayerIds(Layer layer) {
        List<Integer> sublayerIds = new ArrayList<>();
        sublayerIds.add(0);

        if(layer.getSublayers() != null) {
            for (Layer sublayer : layer.getSublayers()) {
                sublayerIds.add(sublayer.getId());
            }
        }

        return sublayerIds;
    }
}
