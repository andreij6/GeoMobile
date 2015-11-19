package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.DaggerFeatureWindowComponent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.FeatureWindowComponent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.IFeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelStateReactor;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public abstract class FeatureTabBase extends GeoViewFragmentBase implements IPanelStateReactor {
    private static final String TAG = FeatureTabBase.class.getSimpleName();

    FeatureQueryResponse mResponse;
    int mLayout;
    IFeatureWindowDataParser DataParser;
    protected LayoutInflater mInflater;
    Context mContext;
    PanelManager mPanelManager;
    @Bind(R.id.moreInfo) TextView mMoreInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mLayout, container, false);
        ButterKnife.bind(this, v);
        mInflater = inflater;
        handleArgs();
        mContext = getActivity();
        FeatureWindowComponent component = DaggerFeatureWindowComponent.builder().build();

        DataParser = component.provideDataParser();

        mPanelManager = new PanelManager(GeoPanel.MAP);

        setDataView();

        if(mPanelManager.isExpanded()){
            Expanded();
        }

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        assert data != null;

        mResponse = data.getFeatureQueryResponse().get(0); //should only be one for a feature window

        String responseJson = new Gson().toJson(mResponse);

        Log.d(TAG, responseJson);


    }

    @Override
    public void Expanded(){
        mMoreInfo.setText(getString(R.string.less_info));
    }

    @Override
    public void Anchored(){
        mMoreInfo.setText(getString(R.string.more_info));
    }

    protected abstract void setDataView();

}
