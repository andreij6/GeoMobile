package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.rest.QueryService;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchRequest;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;
import com.geospatialcorporation.android.geomobile.ui.adapters.QuickSearchAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andre on 6/5/2015.
 */
public class QuickSearchFragment extends GeoViewFragmentBase {

    private static final String TAG = QuickSearchFragment.class.getSimpleName();

    View mView;
    QueryService mService;

    @InjectView(R.id.searchBox) EditText SearchBox;
    @InjectView(R.id.searchRecyclerView) RecyclerView mRecyclerView;

    @OnClick(R.id.close)
    public void close(){
        SlidingPanelManager m = new SlidingPanelManager(getActivity());

        m.collapse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_search_quick, container, false);
        ButterKnife.inject(this, mView);

        SetTitle(R.string.quicksearch);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        SearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendSearch();
                }

                if(mRecyclerView.getVisibility() != View.GONE){
                    mRecyclerView.setVisibility(View.GONE);
                }

                return false;
            }
        });

        mService = application.getRestAdapter().create(QueryService.class);

        return mView;
    }

    private void sendSearch() {
        String query = SearchBox.getText().toString();

        mService.quickSearch(new QuickSearchRequest(query), new Callback<List<QuickSearchResponse>>() {
            @Override
            public void success(List<QuickSearchResponse> response, Response response2) {
                mRecyclerView.setVisibility(View.VISIBLE);

                if(response != null || response.size() == 0) {
                    QuickSearchAdapter adapter = new QuickSearchAdapter(getActivity(), response);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Toaster("No Results");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toaster("Error");
                Log.d(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void onDetach(){
        super.onDetach();
        SetTitle(R.string.app_name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SetTitle(R.string.app_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SetTitle(R.string.app_name);
    }
}
