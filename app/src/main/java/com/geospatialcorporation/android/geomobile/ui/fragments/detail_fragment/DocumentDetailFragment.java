package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/2/2015.
 */
public class DocumentDetailFragment extends ItemDetailFragment<Document>  {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();


    DocumentTreeService mService;

    //region Butterknife
    @InjectView(R.id.backgroundImageView) ImageView mFileTypeImage;
    @InjectView(R.id.documentNameTV) TextView mDocumentName;
    @InjectView(R.id.uploadTimeLabel) TextView mUploadLabel;
    @InjectView(R.id.uploadTimeValue) TextView mUploadValue;
    @InjectView(R.id.fileSizeLabel) TextView mFileSizeLabel;
    @InjectView(R.id.fileSizeValue) TextView mFileSizeValue;
    @InjectView(R.id.fab) FloatingActionButton mFab;
    @InjectView(R.id.backImageView) ImageView mBack;
    @InjectView(R.id.downloadBtn) Button mDownload;

    @OnClick(R.id.fab)
    public void showDocumentActions(){
        GeoDialogHelper.showDocumentActions(getActivity(), mEntity, getActivity().getSupportFragmentManager());
    }

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_document, null);

        ButterKnife.inject(this, view);
        mService = new DocumentTreeService();

        handleArguments();

        mDocumentName.setText(mEntity.getNameWithExt());
        mFileTypeImage.setImageDrawable(getActivity().getResources().getDrawable(mEntity.getFileTypeDrawable(true)));
        mUploadValue.setText(mEntity.getUploadTime());
        mFileSizeValue.setText(mEntity.getSize() + "");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.download(mEntity.getId(), mEntity.getNameWithExt());
            }
        });

        return view;
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Document.INTENT);

        SetTitle(R.string.file_details);
    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            GeoDialogHelper.deleteDocument(getActivity(), mEntity, getFragmentManager());
        }
    };

}
