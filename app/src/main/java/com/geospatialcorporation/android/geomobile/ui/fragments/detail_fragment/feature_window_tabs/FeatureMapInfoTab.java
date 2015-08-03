package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.Implementations.FeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;

import butterknife.InjectView;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureMapInfoTab extends FeatureTabBase {
    private static final String TAG = FeatureMapInfoTab.class.getSimpleName();

    @InjectView(R.id.mapInfoTable) TableLayout mTableLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_mapinfo_tab;
        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapInfoTab());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDataView() {
        FeatureWindowData data = DataParser.parseResponse(mResponse, FeatureWindowDataParser.MAPINFO);

        setTable(data.getList(), mTableLayout, ":");
    }
}
