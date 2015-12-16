package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.rest.QueryService;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchRequest;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResultVM;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.QuickSearchAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QuickSearchPanelFragment extends GeoViewFragmentBase {

    private static final String TAG = QuickSearchPanelFragment.class.getSimpleName();

    QueryService mService;
    DataHelper mDataHelper;

    //region Butterknife
    @Bind(R.id.searchBox) EditText SearchBox;
    @Bind(R.id.searchRecyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.resultCount) TextView mResultCount;
    @Bind(R.id.searchBtn)
    Button mSearch;

    @OnClick(R.id.close)
    public void close(){
        mPanelManager.collapse();
    }

    @OnClick(R.id.searchBtn)
    public void search(){
        sendSearch();
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setView(inflater, container, R.layout.fragment_panel_search_quick);
        setPanelManager(GeoPanel.MAP);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        mDataHelper = new DataHelper();

        mRecyclerView.setLayoutManager(layoutManager);

        mService = application.getRestAdapter().create(QueryService.class);

        return mView;
    }

    //region Helpers
    protected void sendSearch() {
        String query = SearchBox.getText().toString();

        mService.quickSearch(new QuickSearchRequest(query), new Callback<List<QuickSearchResponse>>() {
            @Override
            public void success(List<QuickSearchResponse> response, Response response2) {

                if (response != null || response.size() == 0) {
                    List<QuickSearchResultVM> data = mDataHelper.normalizeQuickSearchResults(response);
                    QuickSearchAdapter adapter = new QuickSearchAdapter(getActivity(), data);
                    mResultCount.setText("Result Count: " + data.size());
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Toaster("No Results");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toaster("Error");
                //Log.d(TAG, error.getMessage());
            }
        });
    }
    //endregion
}
