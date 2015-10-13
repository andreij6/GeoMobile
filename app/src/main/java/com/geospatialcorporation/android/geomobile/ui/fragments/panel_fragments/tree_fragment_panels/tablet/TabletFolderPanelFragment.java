package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;

import butterknife.OnClick;

public class TabletFolderPanelFragment extends TabletTreeFolderPanelFragmentBase {

    IFolderDialog mFolderDialog;

    public TabletFolderPanelFragment(){
        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_panel_document_folder;
    }

    @OnClick(R.id.close)
    public void close(){
        mPanelCtrl.closePanel();
    }

    @OnClick(R.id.renameSection)
    public void rename(){
        mFolderDialog.rename(mFolder, getActivity(), getFragmentManager());

        close();
    }

    @OnClick(R.id.deleteSection)
    public void delete(){
        mFolderDialog.delete(mFolder, getActivity(), getFragmentManager());

        close();
    }
}
