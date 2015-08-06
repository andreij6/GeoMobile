package com.geospatialcorporation.android.geomobile.models;


import com.geospatialcorporation.android.geomobile.models.Layers.Columns;

import java.util.ArrayList;
import java.util.List;

public class AddAttributeRequest {

    List<Columns> Columns;

    public AddAttributeRequest(Columns column){
        Columns = new ArrayList<>();
        Columns.add(column);
    }
}
