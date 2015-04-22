package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.DocumentService;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 4/21/2015.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private static final String TAG = DocumentAdapter.class.getSimpleName();

    Context mContext;
    List<Document> mDocuments;
    DocumentService mService;

    public DocumentAdapter(Context context, List<Document> documents){
        mContext = context;
        mDocuments = documents;
        mService = application.getRestAdapter().create(DocumentService.class);
    }

    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_documents, parent, false);

        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        holder.bindDocumentItem(mDocuments.get(position));
    }

    @Override
    public int getItemCount() {
        if(mDocuments != null)
            return mDocuments.size();
        else
            return 0;
    }

    protected class DocumentViewHolder extends RecyclerView.ViewHolder {

        Document mDocument;
        @InjectView(R.id.documentName) TextView mDocumentName;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindDocumentItem(Document document){
            mDocument = document;

            mDocumentName.setText(mDocument.getName());
        }
    }
}
