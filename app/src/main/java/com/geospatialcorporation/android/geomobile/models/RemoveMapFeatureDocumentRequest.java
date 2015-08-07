package com.geospatialcorporation.android.geomobile.models;


import android.content.Context;

public class RemoveMapFeatureDocumentRequest extends AreYouSureRequest {

    MapFeatureDocumentVM mDoc;
    Integer mLayerId;
    String mFeatureId;
    Context mContext;

    public RemoveMapFeatureDocumentRequest(MapFeatureDocumentVM doc, int layerId, String featureId, Context context){
        mDoc = doc;
        mFeatureId = featureId;
        mLayerId = layerId;
        mContext = context;
    }

    @Override
    public String getMessage(){
        return mMessage + " " + mDoc.getName();
    }

    public MapFeatureDocumentVM getDoc() {
        return mDoc;
    }

    public Integer getLayerId() {
        return mLayerId;
    }

    public String getFeatureId() {
        return mFeatureId;
    }

    public Context getContext(){
        return mContext;
    }
}
