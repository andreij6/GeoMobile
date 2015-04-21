package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.FolderDocumentsActivity;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>{

    private Context mContext;
    private List<Folder> mFolders;

    public LibraryAdapter(Context context, List<Folder> folders){
        mContext = context;
        mFolders = folders;
    }

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_library, parent, false);

        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LibraryViewHolder holder, int position) {
        holder.bindLibraryItem(mFolders.get(position));
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    protected class LibraryViewHolder extends RecyclerView.ViewHolder {
    //regions Properties
    Folder mFolder;
    //endregion

    @InjectView(R.id.folderNameTV) TextView folderName;

    public LibraryViewHolder(View itemView){

        super(itemView);
        ButterKnife.inject(this, itemView);

        itemView.setOnClickListener(FolderOnClickListener);
    }

    public void bindLibraryItem(Folder item){
        mFolder = item;

        folderName.setText(mFolder.getName());
    }

    protected View.OnClickListener FolderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, FolderDocumentsActivity.class)
                                    .putExtra(Folder.FOLDER_INTENT, mFolder);

            mContext.startActivity(intent);
        }
    };
}
}
