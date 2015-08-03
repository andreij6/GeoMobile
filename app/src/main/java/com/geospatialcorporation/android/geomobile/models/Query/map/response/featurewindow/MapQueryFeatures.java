package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;

import java.util.List;

/**
 * Created by andre on 7/2/2015.
 */
public class MapQueryFeatures {

    List<String> FeatureIds;
    Integer Id; //LayerId

    //region Gs & Ss
    public List<String> getFeatureIds() {
        return FeatureIds;
    }

    public void setFeatureIds(List<String> featureIds) {
        FeatureIds = featureIds;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
    //endregion
}
