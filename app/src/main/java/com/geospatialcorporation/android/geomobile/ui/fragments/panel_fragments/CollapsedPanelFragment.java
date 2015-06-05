package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/5/2015.
 */
public class CollapsedPanelFragment extends GeoViewFragmentBase {
    private static final String TAG = CollapsedPanelFragment.class.getSimpleName();

    View mView;


    @InjectView(R.id.title)
    TextView Title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_collapsed, container, false);
        ButterKnife.inject(this, mView);

        return mView;
    }
}
