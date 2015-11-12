package com.geospatialcorporation.android.geomobile.library.services.FeatureWindowCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelStateReactor;

public interface IFeatureWindowCommon {
    FeatureQueryResponse handleArguments(Bundle arguments);

    void tabHost(FragmentTabHost tabHost, Resources resources, Bundle arguments, int currentFeatureWindowTab, int currentTab);

    void setFeatureName(FeatureQueryResponse response, TextView featureName);
}
