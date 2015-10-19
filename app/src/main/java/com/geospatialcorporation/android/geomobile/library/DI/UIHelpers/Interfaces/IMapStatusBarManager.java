package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces;


import java.util.UUID;

public interface IMapStatusBarManager {

    void setMessage(String string);

    void reset();

    void StartLoading(Integer geomtryCode);

    void FinishLoading(Integer geomtryCode);


    void showLayersMessage(String message, UUID uniqeId);

    void finished(int geometryCode, UUID uniqueId);

    void ensureStatusBarVisible();

}
