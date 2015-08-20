package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.support.v4.app.FragmentActivity;

import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

import java.util.List;

public class GetClientsTaskParams extends ExecuterParamsBase {
    List<Subscription> mDataSet;
    int mClientTypeCode;
    FragmentActivity mContext;

    public GetClientsTaskParams(List<Subscription> dataSet, int clientTypeCode, FragmentActivity activity, IPostExecuter executer) {
        super(executer);
        mDataSet = dataSet;
        mClientTypeCode = clientTypeCode;
        mContext = activity;
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
}
