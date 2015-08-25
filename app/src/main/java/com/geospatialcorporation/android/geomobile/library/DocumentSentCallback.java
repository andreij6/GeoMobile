package com.geospatialcorporation.android.geomobile.library;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;

public class DocumentSentCallback implements ISendFileCallback {

    IGeoAnalytics mAnalytics;
    ILayerTreeService mLayerTreeService;
    int mLayerId;
    String mFeatureId;

    public DocumentSentCallback(int layerId, String featureId) {
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mLayerTreeService = application.getTreeServiceComponent().provideLayerTreeService();
        mLayerId = layerId;
        mFeatureId = featureId;
    }

    @Override
    public void invoke(Document document) {
        mAnalytics.trackClick(new GoogleAnalyticEvent().MapfeatureDocument());

        mLayerTreeService.addMapFeatureDocument(mLayerId, mFeatureId, document.getId());
    }
}
