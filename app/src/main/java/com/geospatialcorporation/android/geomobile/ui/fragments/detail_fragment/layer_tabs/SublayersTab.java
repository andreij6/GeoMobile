package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.services.SublayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.SublayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/7/2015.
 */
public class SublayersTab extends GeoDetailsTabBase<Layer> {

    List<Layer> mData;
    @InjectView(R.id.sublayerRecyclerView) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    //@InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_sublayers_tab, container, false);
        ButterKnife.inject(this, v);

        mSwipeRefreshLayout.setOnRefreshListener(new SublayerRefreshListener());
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getActivity().getResources().getColor(R.color.accent));

        mAnalytics.trackScreen(new GoogleAnalyticEvent().SublayersTab());

        //application.setSublayerFragmentPanel(mPanel);
        //mPanel.setBackgroundColor(getActivity().getResources().getColor(R.color.primary_light));

        //ISlidingPanelManager manager = new PanelManager(GeoPanel.SUBLAYER);
        //manager.setup();

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        new GetSublayersTask().execute();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //if(mPanelManager != null) {
        //    mPanelManager.collapse();
        //}
    }

    @Override
    public void refresh() {
        new GetSublayersTask().execute();
    }

    private class GetSublayersTask extends AsyncTask<Void, Void, List<Layer>> {

        @Override
        protected List<Layer> doInBackground(Void... params) {
            mData = new ArrayList<>();

            try {
                mService = new SublayerTreeService();

                if (mEntity != null) {
                    mData = ((SublayerTreeService) mService).getSublayersByLayerId(mEntity.getId());
                }
            } catch (Exception e){
                Toaster(e.getMessage());
            }

            return mData;
        }

        @Override
        protected void onPostExecute(List<Layer> sublayers){
            SublayerAdapter adapter = new SublayerAdapter(getActivity(), mData);

            mRecyclerView.setAdapter(adapter);

            super.onPostExecute(sublayers);
        }


    }

    private class SublayerRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    }
}
