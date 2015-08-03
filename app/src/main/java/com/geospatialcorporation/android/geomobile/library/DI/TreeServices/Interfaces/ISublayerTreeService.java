package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.SublayerCreateRequest;

import java.util.List;

public interface ISublayerTreeService {

    void rename(int id, String name);

    void delete(int sublayerId);

    List<Layer> getSublayersByLayerId(int layerId);

    void createSublayer(SublayerCreateRequest model);
}
