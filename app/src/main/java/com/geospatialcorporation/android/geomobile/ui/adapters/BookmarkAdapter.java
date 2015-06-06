package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Bookmark;

import java.util.List;

/**
 * Created by andre on 6/5/2015.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.Holder> {

    Context mContext;
    List<Bookmark> mData;

    public BookmarkAdapter(Context context, List<Bookmark> data){
        mContext = context;
        mData = data;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_bookmarks, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bindBookmarkItem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        } else {
            return 0;
        }
    }

    protected class Holder extends RecyclerView.ViewHolder{

        public Holder(View itemView) {
            super(itemView);
        }


        public void bindBookmarkItem(Bookmark bookmark) {

        }
    }
}
