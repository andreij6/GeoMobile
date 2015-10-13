package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentDetailPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DocumentDetailFragment extends ItemDetailFragment<Document>  {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();

    //region Butterknife
    @Bind(R.id.uploadTimeLabel) TextView mUploadLabel;
    @Bind(R.id.uploadTimeValue) TextView mUploadValue;
    @Bind(R.id.fileSizeLabel) TextView mFileSizeLabel;
    @Bind(R.id.fileSizeValue) TextView mFileSizeValue;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;


    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @OnClick(R.id.goBackIV)
    public void previousView(){
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.backFolder)
    public void goUp(){ getFragmentManager().popBackStack(); }

    @OnClick(R.id.optionsIV)
    public void bringUpOptions(){
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment f = new DocumentDetailPanelFragment();

            f.setArguments(mEntity.toBundle());
            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();
            mPanelManager.halfAnchor(0.1f);
            mPanelManager.touch(false);
        }
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

        return view;
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Document.INTENT);
    }

    public void closePanel() {
        mPanelManager.hide();
    }
}
