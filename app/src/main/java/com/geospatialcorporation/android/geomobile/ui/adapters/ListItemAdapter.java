package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.tree_activities.DocumentsActivity;
import com.geospatialcorporation.android.geomobile.ui.tree_activities.LayerActivity;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 4/7/2015.
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.LibraryViewHolder>{
    protected final static String TAG = ListItemAdapter.class.getSimpleName();

    public static final String LAYER = "Layer";
    public static final String LIBRARY = "Library";

    private Context mContext;
    private List<ListItem> mListItems;
    private ListItem mListItem;
    private DataHelper mHelper;
    private String mViewType;

    public ListItemAdapter(Context context, List<ListItem> data, String ViewType){
        mContext = context;
        mListItems = data;
        mHelper = new DataHelper();
        mViewType = ViewType;
    }

    @Override
    public LibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_library, parent, false);

        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LibraryViewHolder holder, int position) {
        holder.bindFolder(mListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }



    protected class LibraryViewHolder extends RecyclerView.ViewHolder {
    //regions Properties
    ListItem mItem;
    Folder mFolder;
    //endregion

    @InjectView(R.id.folderNameTV) TextView itemName;

    public LibraryViewHolder(View itemView){
        super(itemView);
        ButterKnife.inject(this, itemView);
        itemView.setOnClickListener(ItemOnClickListener);
    }

    public void bindFolder(ListItem item){
        mItem = item;
        itemName.setText(item.getName());
    }

    protected View.OnClickListener ItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        switch (mItem.getOrder()){
            case ListItem.LAYER:
                goToLayerActivity(mItem);
                break;
            case ListItem.FOLDER:
                goToLibraryActivity(mItem);
                break;
            case ListItem.DOCUMENT:
                goToDocument(mItem);
                break;
            default:
                break;
        }



        }
    };

        private void goToDocument(ListItem item) {
            Toast.makeText(mContext, "You Selected Document " + item.getName(), Toast.LENGTH_LONG).show();
        }

        private void goToLibraryActivity(ListItem item) {
            mFolder = application.getFolderById(mItem.getId());

            Class<?> klass = mViewType == ListItemAdapter.LAYER ? LayerActivity.class : DocumentsActivity.class;

            Intent intent = new Intent(mContext, klass)
                    .putExtra(Folder.FOLDER_INTENT, mFolder);
            mContext.startActivity(intent);

        }

        private void goToLayerActivity(ListItem item) {
            Toast.makeText(mContext, "You Selected Layer " + item.getName(), Toast.LENGTH_LONG).show();
        }


    }
}
