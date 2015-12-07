package com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.AnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.DaggerAnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;

import java.util.List;

public abstract class GeoRecyclerAdapterBase<Holder extends GeoHolderBase<T>, T> extends RecyclerView.Adapter<Holder> {

    private static final String TAG = GeoRecyclerAdapterBase.class.getSimpleName();

    protected List<T> mData;
    protected Context mContext;
    protected Integer mLayoutId;
    private Class<Holder> mClazz;
    protected View mView;
    protected IGeoAnalytics mAnalytics;
    private AnalyticsComponent mAnalyticsComponent;


    public GeoRecyclerAdapterBase(Context context, List<T> data, int layoutId, Class<Holder> clazz){
        mContext = context;
        mLayoutId = layoutId;
        mData = data;
        mClazz = clazz;
        mAnalyticsComponent  = DaggerAnalyticsComponent.builder().build();
        mAnalytics = mAnalyticsComponent.provideGeoAnalytics();
    }

    @Override
    public abstract Holder onCreateViewHolder(ViewGroup parent, int viewType);

    protected void setView(ViewGroup parent, int viewType){
        mView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        try {
            holder.bind(mData.get(position));
        } catch (IndexOutOfBoundsException oe){
            mAnalytics.sendException(oe);
            Log.e(TAG, oe.getMessage());
        } catch ( Exception e){
            Log.e(TAG, e.getMessage());
            mAnalytics.sendException(e);
        }
    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }

        return 0;
    }
}