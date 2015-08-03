package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DocumentDetailFragment extends ItemDetailFragment<Document>  {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();

    DocumentTreeService mService;

    //region Butterknife
    @InjectView(R.id.backgroundImageView) ImageView mFileTypeImage;
    @InjectView(R.id.documentNameTV) TextView mDocumentName;
    @InjectView(R.id.uploadTimeValue) TextView mUploadValue;
    @InjectView(R.id.fileSizeValue) TextView mFileSizeValue;
    @InjectView(R.id.backImageView) ImageView mBack;
    @InjectView(R.id.downloadBtn) Button mDownload;

        mAnalytics.trackClick(new GoogleAnalyticEvent().ShowDocumentActions());

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_document, null);

        ButterKnife.inject(this, view);
        mService = new DocumentTreeService();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().DocumentDetailScreen());

        handleArguments();

        mDocumentName.setText(mEntity.getNameWithExt());
        mFileTypeImage.setImageDrawable(getActivity().getResources().getDrawable(mEntity.getFileTypeDrawable(true)));

        mUploadValue.setText(humanReadableDateTime(mEntity.getUploadTime()));
        mFileSizeValue.setText(humanReadableByteCount(mEntity.getSize(), false));

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

            GeoDialogHelper.deleteDocument(getActivity(), mEntity, getFragmentManager());
        }
    };

    protected static String humanReadableDateTime(String dateTimeString) {
        String usableDateTimeString = dateTimeString;

        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
             usableDateTimeString = format.parse(dateTimeString).toString();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return usableDateTimeString;
    }

    protected static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}