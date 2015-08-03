package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

public interface IGeneralDialog {

    void addMapFeatureDocument(int layerId, String featureId, Context context, FragmentManager manager);

    void editAttributes(Context context, FragmentManager manager);
}
