package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

import java.util.List;

public class GetClientsTaskParams extends ExecuterParamsBase {
    List<Client> mDataSet;
    int mClientTypeCode;
    FragmentActivity mContext;

    public GetClientsTaskParams(List<Client> dataSet, int clientTypeCode, FragmentActivity activity, IPostExecuter executer) {
        super(executer);
        mDataSet = dataSet;
        mClientTypeCode = clientTypeCode;
        mContext = activity;
    }


    public int getClientTypeCode() {
        return mClientTypeCode;
    }

    public List<Client> getDataSet() {
        return mDataSet;
    }

    public FragmentActivity getContext() {
        return mContext;
    }
}
