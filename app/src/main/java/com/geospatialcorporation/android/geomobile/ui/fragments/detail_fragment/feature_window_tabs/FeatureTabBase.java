package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;

/**
 * Created by andre on 7/7/2015.
 */
public abstract class FeatureTabBase extends GeoViewFragmentBase {

    FeatureQueryResponse mResponse;
    int mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mLayout, container, false);
        ButterKnife.inject(this, v);

        handleArgs();

        setRecycler();

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        mResponse = data.getFeatureQueryResponse().get(0); //should only be one for a feature window

    }

    protected abstract void setRecycler();
}
