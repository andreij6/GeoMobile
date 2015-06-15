package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.NodeTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResult;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResultVM;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class QuickSearchAdapter extends GeoRecyclerAdapterBase<QuickSearchAdapter.Holder, QuickSearchResultVM> {

    public QuickSearchAdapter(Context context, List<QuickSearchResultVM> data){
        super(context, data, R.layout.recycler_quick_search, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<QuickSearchResultVM> {

        @InjectView(R.id.nodeTypeIV) ImageView mNodeTypeIV;
        @InjectView(R.id.resultValue) TextView mResult;
        @InjectView(R.id.resultFoundIn) TextView mFoundIn;

        public Holder(View v){ super(v);}

        @Override
        public void bind(QuickSearchResultVM response) {
            mNodeTypeIV.setImageDrawable(mContext.getDrawable(response.getIcon()));
            mResult.setText(response.getResult());
            mFoundIn.setText(response.getFoundIn());
        }

    }

}
