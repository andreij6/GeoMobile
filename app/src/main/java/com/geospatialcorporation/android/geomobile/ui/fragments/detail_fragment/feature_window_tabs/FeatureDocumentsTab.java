package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapFeatureFiles;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureDocumentsTab extends FeatureTabBase {

    int mLayerId;
    String mFeatureId;

    @SuppressWarnings("unused")
    @OnClick(R.id.addDocument)
    public void addDocument(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().ShowAddFeatureDocumentDialog());
        GeoDialogHelper.addMapFeatureDocument(getActivity(), getFragmentManager(), mLayerId, mFeatureId);
    }

    @InjectView(R.id.featureWindowDocumentsTable) TableLayout mTableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_documents_tab;
        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapFeatureDocumentsTab());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDataView() {
        List<FeatureWindowData.KeyValue> data = getDocumentVMList(mResponse.getFeatures().get(0).getFiles());

        mLayerId = mResponse.getId();
        mFeatureId = mResponse.getFeatures().get(0).getId();

        setTable(data, mTableLayout, "");
    }

    protected List<FeatureWindowData.KeyValue> getDocumentVMList(List<MapFeatureFiles> files) {
        List<FeatureWindowData.KeyValue> result = new ArrayList<>();

        if(files != null) {
            for (MapFeatureFiles file : files) {
                result.add(new FeatureWindowData.KeyValue(file.nameWithExt(), file.getSize() + ""));
            }
        }

        return result;
    }
}
