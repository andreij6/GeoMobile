package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.OnClick;

public class QueryPanelFragment extends GeoViewFragmentBase {
    private static final String TAG = QueryPanelFragment.class.getSimpleName();

    @OnClick(R.id.close)
    public void close(){ mPanelManager.hide(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setView(inflater, container, R.layout.fragment_panel_query);
        setPanelManager(GeoPanel.MAP);
        return mView;
    }



}
