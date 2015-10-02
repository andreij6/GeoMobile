package com.geospatialcorporation.android.geomobile.library.services.DocumentDetailCommons;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentDetailPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletLibraryFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class DocumentDetailCommon implements IDocumentDetailCommon {

    Document mEntity;
    Resources mResources;
    ISlidingPanelManager mPanelManager;

    public DocumentDetailCommon(Resources resources){
        mResources = resources;
    }

    @Override
    public IDocumentDetailCommon panel(SlidingUpPanelLayout panel) {
        application.setDocumentDetailFragmentPanel(panel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.DOCUMENT_DETAIL).hide().build();
        return this;
    }

    @Override
    public IDocumentDetailCommon handleArguments(Bundle args) {
        mEntity = args.getParcelable(Document.INTENT);
        return this;
    }

    @Override
    public void setViews(TextView documentName, TextView uploadValue, TextView fileSizeValue, ImageView fileTypeImage) {
        documentName.setText(mEntity.getNameWithExt());
        fileTypeImage.setImageDrawable(mResources.getDrawable(mEntity.getFileTypeDrawable(true)));
        uploadValue.setText(DateTimeFormatter.format(mEntity.getUploadTime()));
        fileSizeValue.setText(FileSizeFormatter.format(mEntity.getSize() + ""));

    }

    @Override
    public void closePanel() {
        mPanelManager.hide();
    }

    @Override
    public void onOptionsButtonPressed(FragmentManager fragmentManager, Fragment fragment) {
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {

            fragment.setArguments(mEntity.toBundle());

            fragmentManager.beginTransaction()
                    .replace(R.id.slider_content, fragment)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        }
    }
}
