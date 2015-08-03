package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersByFolderTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersTaskParams;

public interface IGetLayersTask {
    void getAll(GetLayersTaskParams params);

    void getByFolder(GetLayersByFolderTaskParams params, int folderId);

}
