package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentDetailPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DocumentDetailFragment extends ItemDetailFragment<Document>  {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();

    //region Butterknife
    @Bind(R.id.uploadTimeLabel) TextView mUploadLabel;
    @Bind(R.id.uploadTimeValue) TextView mUploadValue;
    @Bind(R.id.fileSizeLabel) TextView mFileSizeLabel;
    @Bind(R.id.fileSizeValue) TextView mFileSizeValue;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable @Bind(R.id.optionsIV) FloatingActionButton mOptions;

    @Override
    protected void options() {
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment f = new DocumentDetailPanelFragment();

            f.setArguments(mEntity.toBundle());
            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            if(mIsLandscape) {
                mPanelManager.expand();
            } else {
                mPanelManager.halfAnchor(0.1f);
            }

            mPanelManager.touch(false);
        }
    }

    @Override
    protected Fragment getPanelFragment() {
        return new DocumentDetailPanelFragment();
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_document, null);
        ButterKnife.bind(this, view);

        application.setDocumentDetailFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.DOCUMENT_DETAIL).hide().build();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().DocumentDetailScreen());

        handleArguments();

        mTitle.setText(DataHelper.trimString(mEntity.getNameWithExt(), 15));
        mUploadValue.setText(DateTimeFormatter.format(mEntity.getUploadTime()));
        mFileSizeValue.setText(FileSizeFormatter.format(mEntity.getSize() + ""));

        if(mOptions != null) {
            mOptions.bringToFront();
        }

        return view;
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Document.INTENT);
    }
}
