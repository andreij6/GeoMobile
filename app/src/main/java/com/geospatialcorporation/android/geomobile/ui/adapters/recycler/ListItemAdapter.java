package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents.DocumentsAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.IAppDataRepository;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.map.MapActions;
import com.geospatialcorporation.android.geomobile.library.util.Dialogs;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.DocumentFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DownloadDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.List;

import butterknife.InjectView;

public class ListItemAdapter extends GeoRecyclerAdapterBase<ListItemAdapter.Holder, ListItem> {
    protected final static String TAG = ListItemAdapter.class.getSimpleName();

    public static final String LAYER = "Layer";
    public static final String LIBRARY = "Library";

    private String mViewType;
    private FragmentManager mFragmentManager;

    public ListItemAdapter(Context context, List<ListItem> data, String ViewType, FragmentManager fm) {
        super(context, data, R.layout.recycler_list_library, Holder.class);
        mViewType = ViewType;
        mFragmentManager = fm;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<ListItem> {
        //region Properties
        ListItem mItem;
        Folder mFolder;
        //endregion

        @InjectView(R.id.itemNameTV) TextView itemName;
        @InjectView(R.id.itemImageView) ImageButton itemIcon;
        @InjectView(R.id.infoImageView) ImageButton itemInfo;
        @InjectView(R.id.listitem_container) RelativeLayout Container;

        IAppDataRepository<Document> DocumentRepo;
        IAppDataRepository<Folder> FolderRepo;

        public Holder(View itemView) {
            super(itemView);
            itemName.setOnClickListener(ItemOnClickListener);
            itemIcon.setOnClickListener(ItemOnClickListener);
            itemInfo.setOnClickListener(ItemDetailOnClickListener);
            DocumentRepo = new DocumentsAppSource();
            FolderRepo = new FolderAppSource();

        }

        public void bind(ListItem item) {
            mItem = item;
            itemName.setText(item.getName());

            if(item.getIsCompletelyEmpty()) {
                itemIcon.setVisibility(View.GONE);
                itemInfo.setVisibility(View.GONE);
                Container.setBackgroundColor(Color.TRANSPARENT);

            } else if(!item.getShowInfoIcon() && item.getIconId() == 0) {
                itemName.setOnClickListener(null);
                itemIcon.setVisibility(View.GONE);
                itemInfo.setVisibility(View.GONE);
            } else {
                itemIcon.setImageDrawable(mContext.getDrawable(item.getIconId()));

                if(!item.getShowInfoIcon()){
                    itemInfo.setVisibility(View.GONE);  //for parent folder
                }
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

        protected View.OnClickListener ItemDetailOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mItem.getOrder()) {
                    case ListItem.LAYER:
                        LayerDetailAction(mItem);
                        break;
                    case ListItem.FOLDER:
                        FolderDetailAction(mItem);
                        break;
                    case ListItem.DOCUMENT:
                        DocumentDetailAction(mItem);
                        break;
                    default:
                        break;
                }
            }
        };

        //region ListItem Actions by Type
        private void DocumentAction(ListItem item) {
            Document document = DocumentRepo.getById(item.getId());

            DownloadDialogFragment d = new DownloadDialogFragment();
            d.setContext(mContext);
            d.setDocument(document);
            d.show(mFragmentManager, "download");
        }

        private void DocumentDetailAction(ListItem item) {
            Fragment f = new DocumentDetailFragment();
            Document d = DocumentRepo.getById(item.getId());

            Bundle b = new Bundle();
            b.putParcelable(Document.INTENT, d);
            f.setArguments(b);

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.content_frame, f)
                    .commit();
        }

        private void FolderAction(ListItem item) {
            mFolder = FolderRepo.getById(mItem.getId());

            Fragment fragment;

            if (mFolder.getAccessLevel() != null && mFolder.getAccessLevel() < 1) {
                new Dialogs().error("You don't have access to folder: " + mFolder.getName());
            }

            Bundle bundle = new Bundle();
            bundle.putInt(Folder.FOLDER_INTENT, mItem.getId());

            fragment = mViewType.equals(ListItemAdapter.LAYER) ? new LayerFragment() : new DocumentFragment();

            fragment.setArguments(bundle);

            Activity activity = (Activity) itemView.getContext();
            FragmentManager fragmentManager = ((ActionBarActivity)activity).getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .addToBackStack(null)
                    .commit();
        }

        private void FolderDetailAction(ListItem item) {
            Fragment f = mViewType.equals(ListItemAdapter.LAYER) ? new LayerFolderDetailFragment() : new DocumentFolderDetailFragment();
            Folder folder = FolderRepo.getById(item.getId());

            Bundle b = new Bundle();
            b.putParcelable(Folder.FOLDER_INTENT, folder);
            f.setArguments(b);

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.content_frame, f)
                    .commit();
        }

        private void LayerAction(ListItem item) {
            new MapActions().showLayer(item.getId());
        }

        private void LayerDetailAction(ListItem item) {
            Fragment f = new LayerDetailFragment();
            LayerTreeService service = new LayerTreeService();
            Layer layer = service.getLayer(item.getId());

            Bundle b = new Bundle();
            b.putParcelable(Layer.LAYER_INTENT, layer);
            f.setArguments(b);

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.content_frame, f)
                    .commit();
        }
        //endregion
    }


}
