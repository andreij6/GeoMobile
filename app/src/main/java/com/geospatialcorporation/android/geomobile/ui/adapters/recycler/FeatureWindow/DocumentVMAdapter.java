package com.geospatialcorporation.android.geomobile.ui.adapters.recycler.FeatureWindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureDocumentsTab;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by andre on 7/22/2015.
 */
public class DocumentVMAdapter extends GeoRecyclerAdapterBase<DocumentVMAdapter.Holder, FeatureDocumentsTab.FeatureWindowDocumentVM> {

    public DocumentVMAdapter(Context context, List<FeatureDocumentsTab.FeatureWindowDocumentVM> data) {
        super(context, data, R.layout.recycler_feature_window_documents, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<FeatureDocumentsTab.FeatureWindowDocumentVM>{

        @InjectView(R.id.documentName) TextView mDocumentName;
        @InjectView(R.id.documentSize) TextView mDocumentSize;
        @InjectView(R.id.removeDocument) TextView mRemove;

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(FeatureDocumentsTab.FeatureWindowDocumentVM item) {
            mDocumentName.setText(item.getFileName());
            mDocumentSize.setText(item.getFileSize());
            mRemove.setText("Remove"); //May change to button
        }
    }

}
