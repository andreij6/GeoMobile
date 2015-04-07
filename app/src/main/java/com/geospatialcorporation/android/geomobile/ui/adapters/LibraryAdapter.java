package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.models.LibraryItems.LibraryItem;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>{

    private Context mContext;
    private ArrayList<LibraryItem> mLibraryItems;

    public LibraryAdapter(Context context, ArrayList<LibraryItem> libraryItems){
        mContext = context;
        mLibraryItems = libraryItems;
    }

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
    }

    @Override
    public void onBindViewHolder(LibraryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mLibraryItems.size();
    }

    protected class LibraryViewHolder extends RecyclerView.ViewHolder {
    //regions Properties

    //endregion

    public LibraryViewHolder(View itemView){
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
