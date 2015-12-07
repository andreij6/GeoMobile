package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.models.RemoveMapFeatureDocumentRequest;

public interface IGeneralDialog {

    void addMapFeatureDocument(int layerId, String featureId, Context context, FragmentManager manager);

    void editAttributes(Context context, FragmentManager manager);

    void removeMapFeatureDocument(RemoveMapFeatureDocumentRequest request);

    void Logout(Context mainActivity, FragmentManager manager);

    void Subscriptions(Activity activity, FragmentManager supportFragmentManager);
}
