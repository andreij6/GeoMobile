package com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.data.GeoUndergoundProvider;
import com.geospatialcorporation.android.geomobile.data.tables.SubscriptionColumns;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetClientsTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.adapters.cursor.SubscriptionCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class ClientSelectorFragmentBase extends Fragment implements IContentRefresher, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = ClientSelectorFragmentBase.class.getName();

    int mClientTypeCode;
    int mSSPClientTypeCode;
    Context mContext;
    ProgressDialogHelper mProgressHelper;
    protected SubscriptionCursorAdapter mAdapter;

    public void initialize(int clientTypeCode) {
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
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mContext, R.color.white));

        mSSPClientTypeCode = 1;

        preloadSpinner();

        mAdapter = new SubscriptionCursorAdapter(getActivity(), null);

        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    protected void preloadSpinner() {

    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideGetClientsTask();
        mTask.getClients(new GetClientsTaskParams(mDataSet, mClientTypeCode, mSSPClientTypeCode, getActivity()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = {"" + mClientTypeCode, "0"};
        return new CursorLoader(
                getActivity(),
                GeoUndergoundProvider.Subscriptions.CONTENT_URI,
                null,
                SubscriptionColumns.TYPE + " = ? AND " + SubscriptionColumns.SSP + " = ?",
                selectionArgs,
                SubscriptionColumns.NAME + " COLLATE NOCASE ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
