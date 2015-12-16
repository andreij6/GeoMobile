package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.RestoreSettings;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentDetailPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DocumentDetailFragment extends ItemDetailFragment<Document> implements IFragmentView {
    private static final String TAG = DocumentDetailFragment.class.getSimpleName();

    //region Butterknife
    @Bind(R.id.uploadTimeLabel) TextView mUploadLabel;
    @Bind(R.id.uploadTimeValue) TextView mUploadValue;
    @Bind(R.id.fileSizeLabel) TextView mFileSizeLabel;
    @Bind(R.id.fileSizeValue) TextView mFileSizeValue;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable @Bind(R.id.optionsIV) FloatingActionButton mOptions;

    DocumentDetailRestoreSettings mRestoreSettings;
    Bundle mBundle;

    static String RESTORE_SETTINGS_KEY = DocumentDetailRestoreSettings.class.getSimpleName();

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

        if(mIsLandscape){
            mTitle.setText(mEntity.getNameWithExt());
        } else {
            mTitle.setText(DataHelper.trimString(mEntity.getNameWithExt(), 15));
        }

        mUploadValue.setText(DateTimeFormatter.format(mEntity.getUploadTime()));
        mFileSizeValue.setText(FileSizeFormatter.format(mEntity.getSize() + ""));

        if(mOptions != null) {
            mOptions.bringToFront();
        }

        return view;
    }

    @Override
    protected void handleArguments() {
        mBundle = getArguments();

        if(mBundle == null){
            mRestoreSettings = (DocumentDetailRestoreSettings)application.getRestoreSettings(RESTORE_SETTINGS_KEY);
            if(mRestoreSettings != null){
                mEntity = mRestoreSettings.getEntity();
            }
        } else {
            mEntity = mBundle.getParcelable(Document.INTENT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRestoreSettings != null){
            mRestoreSettings.onSaveInstance(mEntity, RESTORE_SETTINGS_KEY);
        } else {
            application.setRestoreSettings(new DocumentDetailRestoreSettings(mEntity), RESTORE_SETTINGS_KEY);
        }
    }

    @Override
    public void setContentFragment(FragmentManager fm, Bundle bundle) {
        setFrame(R.id.content_frame, fm, bundle);
    }

    private void setFrame(int frame, FragmentManager fm, Bundle bundle){
        Fragment fragment = new DocumentDetailFragment();

        if (bundle != null) {
            fragment.setArguments(bundle);


            fm.beginTransaction()
                    .replace(frame, this)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public static class DocumentDetailRestoreSettings extends RestoreSettings<Document> {
        public DocumentDetailRestoreSettings(Document doc){super(doc); }
    }
}
