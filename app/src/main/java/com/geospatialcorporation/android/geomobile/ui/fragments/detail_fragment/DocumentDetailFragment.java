package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentDetailPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DocumentDetailFragment extends ItemDetailFragment<Document>  {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();

    //region Butterknife
    @InjectView(R.id.backgroundImageView) ImageView mFileTypeImage;
    @InjectView(R.id.documentNameTV) TextView mDocumentName;
    @InjectView(R.id.uploadTimeLabel) TextView mUploadLabel;
    @InjectView(R.id.uploadTimeValue) TextView mUploadValue;
    @InjectView(R.id.fileSizeLabel) TextView mFileSizeLabel;
    @InjectView(R.id.fileSizeValue) TextView mFileSizeValue;
    @InjectView(R.id.title) TextView mTitle;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;


    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @OnClick(R.id.navigateBackIV)
    public void previousView(){
        getFragmentManager().popBackStack();
    }

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
            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        }
    }

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_document, null);
        ButterKnife.inject(this, view);

        application.setDocumentDetailFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.DOCUMENT_DETAIL).hide().build();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().DocumentDetailScreen());

        handleArguments();

        mDocumentName.setText(mEntity.getNameWithExt());
        mFileTypeImage.setImageDrawable(getActivity().getResources().getDrawable(mEntity.getFileTypeDrawable(true)));
        mUploadValue.setText(mEntity.getUploadTime());

        mFileSizeValue.setText(FileSizeFormatter.format(mEntity.getSize() + ""));

        return view;
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Document.INTENT);

        SetTitle(R.string.file_details);
    }

    public void closePanel() {
        mPanelManager.hide();
    }
}
