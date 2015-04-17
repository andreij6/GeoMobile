package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>{

    private Context mContext;
    private ArrayList<Document> mDocuments;

    public LibraryAdapter(Context context, ArrayList<Document> documents){
        mContext = context;
        mDocuments = documents;
    }

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_library, parent, false);

        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LibraryViewHolder holder, int position) {
        holder.bindLibraryItem(mDocuments.get(position));
    }

    @Override
    public int getItemCount() {
        return mDocuments.size();
    }

    protected class LibraryViewHolder extends RecyclerView.ViewHolder {
    //regions Properties

    //endregion

    public LibraryViewHolder(View itemView){

        super(itemView);

    }

    public void bindLibraryItem(Document item){

    }

    protected View.OnClickListener LibraryItemClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };
}
}
