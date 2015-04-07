package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.models.LibraryItem;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryItemAdapter extends RecyclerView.Adapter<LibraryItemAdapter.LibraryItemViewHolder>{

    private Context mContext;
    private ArrayList<LibraryItem> mLibraryItems;

    public LibraryItemAdapter(Context context, ArrayList<LibraryItem> libraryItems){
        mContext = context;
        mLibraryItems = libraryItems;
    }

    @Override
    public LibraryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
    }

    @Override
    public void onBindViewHolder(LibraryItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mLibraryItems.size();
    }

    protected class LibraryItemViewHolder extends RecyclerView.ViewHolder {
    //regions Properties

    //endregion

    public LibraryItemViewHolder(View itemView){
        super(itemView);
    }

    public void bindLibraryItem(LibraryItem item){

    }

    protected View.OnClickListener LibraryItemClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };
}
}
