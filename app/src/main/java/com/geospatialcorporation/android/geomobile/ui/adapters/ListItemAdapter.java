package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.map.MapActions;
import com.geospatialcorporation.android.geomobile.library.util.Dialogs;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DownloadDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.LayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.DocumentFragment;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListViewHolder> {
    protected final static String TAG = ListItemAdapter.class.getSimpleName();

    public static final String LAYER = "Layer";
    public static final String LIBRARY = "Library";

    private Context mContext;
    private List<ListItem> mListItems;
    private ListItem mListItem;
    private DataHelper mHelper;
    private String mViewType;
    private FragmentManager mFragmentManager;

    public ListItemAdapter(Context context, List<ListItem> data, String ViewType, FragmentManager fm) {
        mContext = context;
        mListItems = data;
        mHelper = new DataHelper();
        mViewType = ViewType;
        mFragmentManager = fm;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_library, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.bindFolder(mListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    protected class ListViewHolder extends RecyclerView.ViewHolder {
        //region Properties
        ListItem mItem;
        Folder mFolder;
        //endregion

        @InjectView(R.id.itemNameTV) TextView itemName;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(ItemOnClickListener);

        }

        public void bindFolder(ListItem item) {
            mItem = item;
            itemName.setText(item.getName());
            setDrawable(item);

        }

        private void setDrawable(ListItem item) {
            int folder = R.drawable.ic_folder_black_24dp;
            int download = R.drawable.ic_file_download_black_24dp;
            int file = R.drawable.ic_insert_drive_file_black_24dp;
            //int layer = R.drawable.ic_layers_black_24dp;  Change to Get a layer type drawable


            switch (item.getOrder()){
                case ListItem.DOCUMENT:
                    itemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, download, 0);
                    break;
                case ListItem.FOLDER:
                    //itemName.setCompoundDrawablesWithIntrinsicBounds(folder, 0, 0, 0);
                    break;
                case ListItem.LAYER:
                    //itemName.setCompoundDrawablesWithIntrinsicBounds(layer, 0, 0, 0);
                    break;
            }

        }

        protected View.OnClickListener ItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mItem.getOrder()) {
                    case ListItem.LAYER:
                        LayerAction(mItem);
                        break;
                    case ListItem.FOLDER:
                        FolderAction(mItem);
                        break;
                    case ListItem.DOCUMENT:
                        DocumentAction(mItem);
                        break;
                    default:
                        break;
                }
            }
        };

        private void DocumentAction(ListItem item) {
            Document document = application.getDocumentById(item.getId());

            DownloadDialogFragment d = new DownloadDialogFragment();
            d.setContext(mContext);
            d.setDocument(document);
            d.show(mFragmentManager, "download");
        }

        private void FolderAction(ListItem item) {
            mFolder = application.getFolderById(mItem.getId());

            Fragment fragment;

            if (mFolder.getAccessLevel() != null && mFolder.getAccessLevel() < 1) {
                new Dialogs().error("You don't have access to folder: " + mFolder.getName());
            }

            Bundle bundle = new Bundle();
            bundle.putInt(Folder.FOLDER_INTENT, mItem.getId());

            if (mViewType.equals(ListItemAdapter.LAYER)) {
                // Layer
                fragment = new LayerFragment();
                fragment.setArguments(bundle);
            } else {
                // Documents
                fragment = new DocumentFragment();
                fragment.setArguments(bundle);
            }

            Activity activity = (Activity) itemView.getContext();
            FragmentManager fragmentManager = activity.getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .commit();
        }

        private void LayerAction(ListItem item) {
            new MapActions().showLayer(item.getId());
        }
    }


}
