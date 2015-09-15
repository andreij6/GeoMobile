package com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 7/7/2015.
 */
public class ParcelableFeatureQueryResponse implements Parcelable {

    private static final String TAG = ParcelableFeatureQueryResponse.class.getSimpleName();

    //region Constructors
    public ParcelableFeatureQueryResponse(List<FeatureQueryResponse> response) {
        mResponse = response;
    }

    private ParcelableFeatureQueryResponse(Parcel in){
        mResponse = new ArrayList<>();
        in.readList(mResponse, null);
    }
    //endregion

    List<FeatureQueryResponse> mResponse;
    public static final String FEATURE_QUERY_RESPONSE = "FeatureQueryResponse";

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeList(mResponse);
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }

    }

    public static final Creator<ParcelableFeatureQueryResponse> CREATOR = new Creator<ParcelableFeatureQueryResponse>(){

        @Override
        public ParcelableFeatureQueryResponse createFromParcel(Parcel source) {
            return new ParcelableFeatureQueryResponse(source);
        }

        @Override
        public ParcelableFeatureQueryResponse[] newArray(int size) {
            return new ParcelableFeatureQueryResponse[size];
        }
    };
    //endregion

    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putParcelable(FEATURE_QUERY_RESPONSE, this);
        return b;
    }

    public List<FeatureQueryResponse> getFeatureQueryResponse() {
        return mResponse;
    }

}
