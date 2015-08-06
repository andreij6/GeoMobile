package com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces;

import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.BookmarkMapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.google.android.gms.maps.model.CameraPosition;

public interface IMapStateService {
    void saveMapState(MapStateSaveRequest request);

    CameraPosition getSavedCameraPosition();

    Integer getSavedMapType();

    void saveBookmarkState(BookmarkMapStateSaveRequest request);
}
