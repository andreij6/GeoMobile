package com.geospatialcorporation.android.geomobile.ui.Interfaces;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;

public interface IFeatureWindowCtrl {
    void showFeatureWindow(ParcelableFeatureQueryResponse response);
}
