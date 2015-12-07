package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces;

import com.geospatialcorporation.android.geomobile.models.AddAttributeRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.EditLayerAttributesRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.models.RemoveMapFeatureDocumentRequest;

import java.util.List;

public interface ILayerTreeService {
    void create(String name, int shape, int folderId);

    void delete(int id);

    Layer get(int id);

    void rename(int id, String name);

    LayerDetailsVm getDetails(int id);

    List<LayerAttributeColumn> getColumns(int layerId);

    void addColumn(int id, AddAttributeRequest data);

    void deleteColumn(int layerId, int columnId);

    void addMapFeatureDocument(int layerId, String featureId, int documentId);

    void removeMapFeatureDocument(RemoveMapFeatureDocumentRequest request);

    void editAttributeValue(int layerId, EditLayerAttributesRequest request);
}
