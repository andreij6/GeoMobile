package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResultVM;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;

import java.util.List;

import butterknife.Bind;

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

        @Bind(R.id.nodeTypeIV) ImageView mNodeTypeIV;
        @Bind(R.id.resultValue) TextView mResult;
        @Bind(R.id.resultFoundIn) TextView mFoundIn;

        public Holder(View v){ super(v);}

        @Override
        public void bind(QuickSearchResultVM response) {
            mNodeTypeIV.setImageDrawable(ContextCompat.getDrawable(mContext, response.getIcon()));
            mResult.setText(response.getResult());
            mFoundIn.setText(response.getFoundIn());
        }

    }

}
