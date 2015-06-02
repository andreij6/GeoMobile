package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/2/2015.
 */
public class DocumentDetailFragment extends ItemDetailFragment<Document> {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();

    Document mDocument;

    //view
    @InjectView(R.id.documentNameTV) TextView mDocumentName;
    @InjectView(R.id.deleteDocumentIcon) ImageView mDeleteIcon;
    @InjectView(R.id.deleteDocumentTV) TextView mDeleteText;
    @InjectView(R.id.backImageView) ImageView mBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.document_detail_fragment, null);

        ButterKnife.inject(this, view);

        HandleArguments();

        SetupUI();

        return view;
    }

    @Override
    protected void HandleArguments() {
        Bundle args = getArguments();

        mDocument = args.getParcelable(Document.INTENT);
    }

    @Override
    protected void SetupUI(){
        mDocumentName.setText(mDocument.getNameWithExt());

        mDeleteIcon.setOnClickListener(DeleteonClickListner);
        mDeleteText.setOnClickListener(DeleteonClickListner);

        mBack.setOnClickListener(BackButtonClicked);
    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Toast.makeText(getActivity(), "Delete Not Implemented", Toast.LENGTH_LONG).show();
        }
    };
}
