package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetClientsTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.ClientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ClientSelectorFragment extends Fragment
        implements IContentRefresher, IPostExecuter<List<Subscription>>
{
    private static final String TAG = ClientSelectorFragment.class.getName();

    int mClientTypeCode;
    Context mContext;
    ProgressDialogHelper mProgressHelper;

    public ClientSelectorFragment initialize(int clientTypeCode, Context context){
        this.mClientTypeCode = clientTypeCode;
        this.mContext = context;
        return this;
    }

    @InjectView(R.id.clientitem_recyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Subscription> mDataSet;
    IGetClientsTask mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;

        mRootView = inflater.inflate(R.layout.fragment_clientitems, container, false);
        ButterKnife.inject(this, mRootView);

        mDataSet = new ArrayList<>();
        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();
        mProgressHelper = new ProgressDialogHelper(getActivity());

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        refresh();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    @Override
    public void refresh() {
        mProgressHelper.showProgressDialog();
        mTask = application.getTasksComponent().provideGetClientsTask();
        mTask.getClients(new GetClientsTaskParams(mDataSet, mClientTypeCode, getActivity(), this));
    }

    @Override
    public void onPostExecute(List<Subscription> model) {
        ClientAdapter adapter = new ClientAdapter(mContext, model);

        mRecyclerView.setAdapter(adapter);

        mProgressHelper.hideProgressDialog();
    }
}
