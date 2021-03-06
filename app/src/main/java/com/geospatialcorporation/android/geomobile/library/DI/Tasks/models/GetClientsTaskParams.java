package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.support.v4.app.FragmentActivity;

import com.geospatialcorporation.android.geomobile.models.Subscription;

import java.util.List;

public class GetClientsTaskParams {
    List<Subscription> mDataSet;
    int mClientTypeCode;
    FragmentActivity mContext;
    private int mSSPClientTypeCode;

    public GetClientsTaskParams(List<Subscription> dataSet, int clientTypeCode, int sspClientTypeCode, FragmentActivity activity) {
        mDataSet = dataSet;
        mClientTypeCode = clientTypeCode;
        mContext = activity;
        mSSPClientTypeCode = sspClientTypeCode;
    }


    public int getClientTypeCode() {
        return mClientTypeCode;
    }

    public List<Subscription> getDataSet() {
        return mDataSet;
    }

    public FragmentActivity getContext() {
        return mContext;
    }

    public int getSSPClientTypeCode() {
        return mSSPClientTypeCode;
    }
}
