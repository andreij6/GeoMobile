package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents.DocumentsAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabDocumentDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabDocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabLayerFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DownloadDialogFragment;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet.TabLayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet.TabLibraryFragment;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.Bind;

public class ListItemAdapter extends GeoRecyclerAdapterBase<ListItemAdapter.Holder, ListItem> {
    protected final static String TAG = ListItemAdapter.class.getSimpleName();

    public static final String LAYER = "Layer";
    public static final String LIBRARY = "Library";

    private String mViewType;
    private FragmentManager mFragmentManager;
    private ISlidingPanelManager mPanelManager;

    public ListItemAdapter(Context context, List<ListItem> data, String ViewType, FragmentManager fm, ISlidingPanelManager panelManager) {
        super(context, data, R.layout.recycler_list_library, Holder.class);
        mViewType = ViewType;
        mFragmentManager = fm;
        mPanelManager = panelManager;
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

        @Bind(R.id.itemNameTV) TextView itemName;
        @Bind(R.id.itemImageView) FloatingActionButton itemIcon;
        @Bind(R.id.infoImageView) ImageButton itemInfo;
        @Bind(R.id.listitem_container) RelativeLayout Container;

        IFullDataRepository<Document> DocumentRepo;
        IFullDataRepository<Folder> FolderRepo;

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

            itemName.setText(DataHelper.trimString(item.getName(), 15));

            itemIcon.setVisibility(View.VISIBLE);
            itemInfo.setVisibility(View.VISIBLE);
            Container.setBackgroundColor(Color.WHITE);

            if(item.getIsCompletelyEmpty()) {
                itemIcon.setVisibility(View.INVISIBLE);
                itemInfo.setVisibility(View.INVISIBLE);
                Container.setBackgroundColor(Color.TRANSPARENT);

            } else if(!item.getShowInfoIcon() && item.getIconId() == 0) {
                itemName.setOnClickListener(null);
                itemIcon.setVisibility(View.INVISIBLE);
                itemInfo.setVisibility(View.INVISIBLE);
            } else {

                if(item.getDrawable() != null){
                    itemIcon.setImageDrawable(item.getDrawable());
                } else {
                    itemIcon.setImageDrawable(ContextCompat.getDrawable(mContext, item.getIconId()));
                }


                if(!item.getShowInfoIcon()){
                    itemInfo.setVisibility(View.INVISIBLE);  //for parent folder
                }
            }

        }

        protected View.OnClickListener ItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPanelManager.getIsOpen()){
                    mPanelManager.hide();
                } else {

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
            }
        };

        protected View.OnClickListener ItemDetailOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPanelManager.getIsOpen()){
                    mPanelManager.hide();
                } else {

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
            }
        };

        //region ListItem Actions by Type
        protected void DocumentAction(ListItem item) {
            Document document = DocumentRepo.getById(item.getId());

            DownloadDialogFragment d = new DownloadDialogFragment();
            d.setContext(mContext);
            d.setDocument(document);
            d.show(mFragmentManager, "download");

        }

        protected void DocumentDetailAction(ListItem item) {
            Fragment f;
            int info_frame;

            Document d = DocumentRepo.getById(item.getId());

            if(DeviceTypeUtil.isTablet(mContext.getResources())){
                f = new TabDocumentDetailFragment();

                info_frame = R.id.info_frame;
            } else {
                f = new DocumentDetailFragment();

                info_frame = R.id.content_frame;
            }

            f.setArguments(d.toBundle());

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(info_frame, f)
                    .commit();
        }

        protected void FolderAction(ListItem item) {
            mFolder = FolderRepo.getById(mItem.getId());

            Fragment fragment;

            Bundle bundle = new Bundle();
            bundle.putInt(Folder.FOLDER_INTENT, mItem.getId());

            Activity activity = (Activity) itemView.getContext();
            FragmentManager fragmentManager = ((ActionBarActivity) activity).getSupportFragmentManager();
            int info_frame = 0;

            if(DeviceTypeUtil.isTablet(mContext.getResources())){
                fragment = mViewType.equals(ListItem.LAYER) ? new TabLayerFragment() : new TabLibraryFragment();

                info_frame = R.id.info_frame;

            } else {

                if (mFolder != null && mFolder.getAccessLevel() != null && mFolder.getAccessLevel() < 1) {
                    //new Dialogs().error("You don't have access to folder: " + mFolder.getName());
                }

                fragment = mViewType.equals(ListItemAdapter.LAYER) ? new LayerFragment() : new LibraryFragment();

                info_frame = R.id.content_frame;
            }

            fragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(info_frame, fragment)
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .addToBackStack(null)
                    .commit();
        }

        protected void FolderDetailAction(ListItem item) {
            int infoFrame = 0;
            Fragment f;


            if(DeviceTypeUtil.isTablet(mContext.getResources())){
                infoFrame = R.id.info_frame;
                f = mViewType.equals(ListItemAdapter.LAYER) ? new TabLayerFolderDetailFragment() : new TabDocumentFolderDetailFragment();
            } else {
                infoFrame = R.id.content_frame;
                f = mViewType.equals(ListItemAdapter.LAYER) ? new LayerFolderDetailFragment() : new DocumentFolderDetailFragment();
            }

            Folder folder = FolderRepo.getById(item.getId());

            f.setArguments(folder.toBundle());

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(infoFrame, f)
                    .commit();

        }

        protected void LayerAction(ListItem item) {
            toLayerDetailView(item);
        }

        protected void LayerDetailAction(ListItem item) {
            toLayerDetailView(item);
        }
        //region Helper
        protected void toLayerDetailView(ListItem item) {
            if(DeviceTypeUtil.isTablet(mContext.getResources())){
                Toast.makeText(mContext, "is tablet", Toast.LENGTH_LONG).show();
            } else {
                Fragment f = new LayerDetailFragment();
                ILayerTreeService service = application.getTreeServiceComponent().provideLayerTreeService();
                Layer layer = service.get(item.getId());

                f.setArguments(layer.toBundle());

                mFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, f)
                        .commit();
            }
        }
        //endregion
        //endregion
    }

}
