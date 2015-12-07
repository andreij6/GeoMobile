package com.geospatialcorporation.android.geomobile.models.Query.map;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

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

    List<Integer> SublayerIds;
    List<String> FeatureIds;
    Integer Id; //LayerId

    public Layers(Layer layer){
        Id = layer.getId();
        SublayerIds = getSublayerIds(layer);
    }

    public Layers(List<String> featureIds, Integer id){
        Id = id;
        FeatureIds = featureIds;
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

    //region Gs & Ss
    public List<String> getFeatureIds() {
        return FeatureIds;
    }

    public void setFeatureIds(List<String> featureIds) {
        FeatureIds = featureIds;
    }
    //endregion
}
