package com.geospatialcorporation.android.geomobile.models.Layers;

import com.geospatialcorporation.android.geomobile.models.Item.ItemCreateRequest;

/**
 * Created by andre on 6/1/2015.
 */
public class LayerCreateRequest extends ItemCreateRequest {
    public int getGeometryType() {
        return GeometryType;
    }

    public void setGeometryType(int geometryType) {
        GeometryType = geometryType;
    }

    //PluginCodes Plugin?
    int GeometryType;
    //ExtentModel Extent;
    public LayerCreateRequest(int code, String name, int parentId){
        setName(name);
        setGeometryType(code);
        setParentId(parentId);
    }

}
