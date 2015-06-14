package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;

import java.util.List;

public class BookmarkAdapter extends GeoRecyclerAdapterBase<BookmarkAdapter.Holder, Bookmark> {

    public BookmarkAdapter(Context context, List<Bookmark> data){
        super(context, data, R.layout.recycler_bookmarks, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }


    protected class Holder extends GeoHolderBase<Bookmark> {

        public Holder(View itemView) {
            super(itemView);
        }


        public void bind(Bookmark bookmark) {

        }
    }
}
