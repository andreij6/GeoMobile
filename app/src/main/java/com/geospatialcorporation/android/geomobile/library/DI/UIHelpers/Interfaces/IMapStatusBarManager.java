package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces;


public interface IMapStatusBarManager {

    void setMessage(String string);

    void setLayerMessage(String layerMessage);

    void reset();

    void removeLayer(String name);
}
