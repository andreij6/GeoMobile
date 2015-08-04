package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DocumentDetailFragment extends ItemDetailFragment<Document>  {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();


    IDocumentTreeService mService;
    IDocumentDialog mDocumentDialog;

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
        mAnalytics.trackClick(new GoogleAnalyticEvent().ShowDocumentActions());

        mDocumentDialog.actions(mEntity, getActivity(), getActivity().getSupportFragmentManager());
    }

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_document, null);

        ButterKnife.inject(this, view);

        mService = application.getTreeServiceComponent().provideDocumentTreeService();
        mDocumentDialog = application.getUIHelperComponent().provideDocumentDialog();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().DocumentDetailScreen());

        handleArguments();

        mDocumentName.setText(mEntity.getNameWithExt());
        mFileTypeImage.setImageDrawable(getActivity().getResources().getDrawable(mEntity.getFileTypeDrawable(true)));
        mUploadValue.setText(mEntity.getUploadTime());
        mFileSizeValue.setText(FileSizeFormatter.format(mEntity.getSize() + ""));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mAnalytics.trackClick(new GoogleAnalyticEvent().DownloadDocument());

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
            mAnalytics.trackClick(new GoogleAnalyticEvent().DeleteDocument());

            mDocumentDialog.delete(mEntity, getActivity(), getFragmentManager());
        }
    };

}
