package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.ButterKnife;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureMapInfoTab extends FeatureTabBase {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_mapinfo_tab;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setRecycler() {

    }


}
