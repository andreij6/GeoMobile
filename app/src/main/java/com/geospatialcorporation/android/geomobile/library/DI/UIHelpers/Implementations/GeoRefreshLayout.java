package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;

public class GeoRefreshLayout implements ILayoutRefresher {

    public SwipeRefreshLayout.OnRefreshListener build(SwipeRefreshLayout refreshLayout, IContentRefresher refresher){
        return new Refresher(refreshLayout, refresher);
    }

    private class Refresher implements SwipeRefreshLayout.OnRefreshListener{

        SwipeRefreshLayout mSwipeRefreshLayout;
        IContentRefresher mRefresher;

        public Refresher(SwipeRefreshLayout refreshLayout, IContentRefresher refresher){
            mSwipeRefreshLayout = refreshLayout;
            mRefresher = refresher;
        }

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefresher.refresh();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    }


}
