package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces;


import java.util.UUID;

public interface IMapStatusBarManager {

    void setMessage(String string);

    void reset();

    void StartLoading(Integer geomtryCode);

    void finished(int geometryCode, UUID uniqueId);

    void showLayersProgress(UUID uniqeId);

    void FinishLoading(Integer geomtryCode);
}
