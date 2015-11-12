package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;

public class VideoDetailsTab extends FeatureTabBase {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_videodetails_tab;
        //mAnalytics.trackScreen(new GoogleAnalyticEvent().MapInfoTab());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDataView() {

    }
}
