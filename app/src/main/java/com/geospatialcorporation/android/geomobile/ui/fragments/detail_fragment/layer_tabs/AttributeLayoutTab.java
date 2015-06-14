package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.AsyncTask;
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
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumns;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.AttributeAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/7/2015.
 */
public class AttributeLayoutTab extends GeoDetailsTabBase<Layer> {

    private static final String TAG = AttributeLayoutTab.class.getSimpleName();

    List<LayerAttributeColumns> mAttributeColumns;

    @InjectView(R.id.attributeRecycler) RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.fab) FloatingActionButton mFab;

    @OnClick(R.id.fab)
    public void addColumn(){
        Toaster("Add a Attribute Column");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_attributes_tab, container, false);
        ButterKnife.inject(this, v);

        mSwipeRefreshLayout.setOnRefreshListener(new AttributeRefreshListener());
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getActivity().getResources().getColor(R.color.accent));

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        new GetAttributeColumns().execute();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return v;
    }

    private class GetAttributeColumns extends AsyncTask<Void, Void, List<LayerAttributeColumns>> {

        @Override
        protected List<LayerAttributeColumns> doInBackground(Void... params) {
            mService = new LayerTreeService();

            if(mEntity != null){
                Log.d(TAG, "LAYER not Null");
                mAttributeColumns = ((LayerTreeService)mService).getLayerAttributeColumns(mEntity.getId());
            }

            Log.d(TAG, "Returning Attr Columns");
            return mAttributeColumns;
        }

        @Override
        protected void onPostExecute(List<LayerAttributeColumns> attributes){
            AttributeAdapter adapter = new AttributeAdapter(getActivity(), attributes);

            mRecyclerView.setAdapter(adapter);
        }

    }

    private class AttributeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new GetAttributeColumns().execute();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    }

}
