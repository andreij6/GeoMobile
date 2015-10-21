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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetClientsTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.ClientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientSelectorFragment extends Fragment
        implements IContentRefresher, IPostExecuter<List<Subscription>>
{
    private static final String TAG = ClientSelectorFragment.class.getName();

    int mClientTypeCode;
    int mSSPClientTypeCode;
    Context mContext;
    ProgressDialogHelper mProgressHelper;

    public void initialize(int clientTypeCode){
        mClientTypeCode = clientTypeCode;
        mContext = getActivity();
    }

    @Bind(R.id.clientitem_recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.ssp_client_spinner) Spinner mSSPClientSpinner;
    private List<Subscription> mDataSet;
    IGetClientsTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;

        mRootView = inflater.inflate(R.layout.fragment_clientitems, container, false);
        ButterKnife.bind(this, mRootView);

        mDataSet = new ArrayList<>();
        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();
        mProgressHelper = new ProgressDialogHelper(getActivity());

        mSwipeRefreshLayout.setOnRefreshListener(refresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(mContext.getResources().getColor(R.color.accent));

        mSSPClientTypeCode = 1;

        preloadSpinner();

        refresh();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    private void preloadSpinner() {
        if(mClientTypeCode == ClientTypeCodes.SSP.getKey()){
            mSSPClientSpinner.setVisibility(View.VISIBLE);
        } else {
            return;
        }

        final ArrayList<String> list = new ArrayList<>();

        list.add("Administrator");
        list.add("Regional Administrators");
        list.add("Project Manager");
        list.add("SSP Clients");

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mContext, R.array.ssp_clients, R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        //new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSSPClientSpinner.setAdapter(dataAdapter);

        mSSPClientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSSPClientTypeCode = position + 1;

                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void refresh() {
        mProgressHelper.showProgressDialog();

        mTask = application.getTasksComponent().provideGetClientsTask();
        mTask.getClients(new GetClientsTaskParams(mDataSet, mClientTypeCode, mSSPClientTypeCode, getActivity(), this));
    }

    @Override
    public void onPostExecute(List<Subscription> model) {
        ClientAdapter adapter = new ClientAdapter(mContext, model);

        mRecyclerView.setAdapter(adapter);

        mProgressHelper.hideProgressDialog();
    }
}
