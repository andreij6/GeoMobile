package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.DaggerFeatureWindowComponent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.FeatureWindowComponent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.IFeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

public abstract class TabletFeatureTabBase extends TabGeoViewFragmentBase {

    protected FeatureQueryResponse mResponse;
    protected LayoutInflater mInflater;
    protected IFeatureWindowDataParser DataParser;
    protected IFolderTreeService mFolderTreeService;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FeatureWindowComponent component = DaggerFeatureWindowComponent.builder().build();

        DataParser = component.provideDataParser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mFolderTreeService = application.getTreeServiceComponent().provideFolderTreeService();

        mInflater = inflater;

        handleArgs();

        setDataView();

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        assert data != null;

        mResponse = data.getFeatureQueryResponse().get(0); //should only be one for a feature window

    }

    protected abstract void setDataView();
}
