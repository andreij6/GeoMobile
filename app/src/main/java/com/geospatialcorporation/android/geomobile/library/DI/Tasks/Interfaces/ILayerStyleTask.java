package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces;

import com.geospatialcorporation.android.geomobile.library.map.GeoCallback;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;

public interface ILayerStyleTask {
    void getStyle(LegendLayer layer, GeoCallback callback);
}
