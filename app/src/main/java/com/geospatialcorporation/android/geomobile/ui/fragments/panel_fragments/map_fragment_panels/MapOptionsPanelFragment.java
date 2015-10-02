package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapOptionsPanelFragment extends GeoViewFragmentBase {


    @OnClick(R.id.createFeatureSection)
    public void createFeature(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_panel_mapoptions, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }
}
