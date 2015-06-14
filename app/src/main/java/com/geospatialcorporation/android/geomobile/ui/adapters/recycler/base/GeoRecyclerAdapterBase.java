package com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by andre on 6/14/2015.
 */

public abstract class GeoRecyclerAdapterBase<Holder extends GeoHolderBase<T>, T> extends RecyclerView.Adapter<Holder> {

    private static final String TAG = GeoRecyclerAdapterBase.class.getSimpleName();

    protected List<T> mData;
    protected Context mContext;
    protected Integer mLayoutId;
    private Class<Holder> mClazz;
    protected View mView;

    public GeoRecyclerAdapterBase(Context context, List<T> data, int layoutId, Class<Holder> clazz){
        mContext = context;
        mLayoutId = layoutId;
        mData = data;
        mClazz = clazz;
    }

    @Override
    public abstract Holder onCreateViewHolder(ViewGroup parent, int viewType);

    protected void setView(ViewGroup parent, int viewType){
        mView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }

        return 0;
    }
}