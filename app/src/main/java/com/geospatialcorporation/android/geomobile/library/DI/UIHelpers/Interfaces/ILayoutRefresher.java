package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces;

import android.support.v4.widget.SwipeRefreshLayout;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;

public interface ILayoutRefresher {
    SwipeRefreshLayout.OnRefreshListener build(SwipeRefreshLayout refreshLayout, IContentRefresher refresher);
}
