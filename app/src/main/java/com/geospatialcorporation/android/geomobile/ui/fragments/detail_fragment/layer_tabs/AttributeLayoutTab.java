package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.AttributeAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AttributeLayoutTab extends GeoDetailsTabBase<Layer> {

    private static final String TAG = AttributeLayoutTab.class.getSimpleName();

    List<LayerAttributeColumn> mAttributeColumns;

    @InjectView(R.id.attributeRecycler) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    //@InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_attributes_tab, container, false);
        ButterKnife.inject(this, v);

        mSwipeRefreshLayout.setOnRefreshListener(new AttributeRefreshListener());
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getActivity().getResources().getColor(R.color.accent));

        //application.setLayerAttributePanel(mPanel);
        //mPanelManager = new PanelManager(GeoPanel.LAYER_ATTRIBUTE);
        //mPanelManager.setup();

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        refresh();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return v;
    }

    @Override
    public void refresh() {
        new GetAttributeColumns().execute();
    }

    private class GetAttributeColumns extends GeoAsyncTask<Void, Void, List<LayerAttributeColumn>> {

        @Override
        protected List<LayerAttributeColumn> doInBackground(Void... params) {
            mService = new LayerTreeService();

            if(mEntity != null){
                Log.d(TAG, "LAYER not Null");
                mAttributeColumns = ((LayerTreeService)mService).getLayerAttributeColumns(mEntity.getId());
            }

            Log.d(TAG, "Returning Attr Columns");
            return mAttributeColumns;
        }

        @Override
        protected void onPostExecute(List<LayerAttributeColumn> attributes){
            AttributeAdapter adapter = new AttributeAdapter(getActivity(), attributes);

            mRecyclerView.setAdapter(adapter);

            super.onPostExecute(attributes);
        }

    }

    private class AttributeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

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
