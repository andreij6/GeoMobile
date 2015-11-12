package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.library.constants.AccessLevelCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.models.Document.Document;

import butterknife.Bind;
import butterknife.OnClick;

public class TabletDocumentDetailPanelFragment extends TabletPanelFragmentBase {

    Document mDocument;
    IDocumentDialog mDocumentDialog;
    Context mContext;
    IDocumentTreeService mService;

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.renameSection) LinearLayout mRenameSection;
    @Bind(R.id.moveSection) LinearLayout mMoveSection;
    @Bind(R.id.deleteSection) LinearLayout mDeleteSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = super.onCreateView(inflater, container, savedInstance);

        mContext = getActivity();
        mDocumentDialog = application.getUIHelperComponent().provideDocumentDialog();
        mService = application.getTreeServiceComponent().provideDocumentTreeService();

        handleArgs();

        if(mDocument.getParentFolder().getAccessLevel() == AccessLevelCodes.ReadOnly){
            mRenameSection.setVisibility(View.GONE);
            mMoveSection.setVisibility(View.GONE);
            mDeleteSection.setVisibility(View.GONE);
        }

        return view;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mDocument = args.getParcelable(Document.INTENT);

        mTitle.setText(DataHelper.trimString(mDocument.getNameWithExt(), 15));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_panel_document_detail;
    }

    @OnClick(R.id.downloadSection)
    public void download(){
        mService.download(mDocument.getId(), mDocument.getNameWithExt());

        close();
    }

    @OnClick(R.id.renameSection)
    public void rename(){
        mDocumentDialog.rename(mDocument, mContext, getFragmentManager());

        close();
    }

    @OnClick(R.id.moveSection)
    public void move(){
        mDocumentDialog.move(mDocument, mContext, getFragmentManager());

        close();
    }

    @OnClick(R.id.deleteSection)
    public void delete(){
        mDocumentDialog.delete(mDocument, mContext, getFragmentManager());

        close();
    }

    @OnClick(R.id.close)
    public void close(){
        mPanelCtrl.closePanel();
    }
}
