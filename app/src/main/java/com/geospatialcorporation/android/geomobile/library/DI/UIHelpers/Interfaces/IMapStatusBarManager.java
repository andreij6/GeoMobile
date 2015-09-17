package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces;


public interface IMapStatusBarManager {

    void setMessage(String string);

    void reset();

    void StartLoading(Integer geomtryCode);

    void FinishLoading(Integer geomtryCode);


}
