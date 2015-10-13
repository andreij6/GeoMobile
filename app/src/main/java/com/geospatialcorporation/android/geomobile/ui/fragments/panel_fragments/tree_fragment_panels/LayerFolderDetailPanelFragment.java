package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerFolderDetailFragment;

import butterknife.OnClick;

public class LayerFolderDetailPanelFragment extends TreeFolderPanelFragmentBase<LayerFolderDetailFragment> {
    IFolderDialog mFolderDialog;

    public LayerFolderDetailPanelFragment(){
        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();
    }

    @Override
    protected int getViewResource() {
        return R.layout.fragment_panel_document_folder;
    }

    @OnClick(R.id.close)
    public void close(){
        mContentFragment.closePanel();
    }

    @OnClick(R.id.renameSection)
    public void rename(){
        mFolderDialog.rename(mFolder, getActivity(), getFragmentManager());

        mContentFragment.closePanel();
    }

    @OnClick(R.id.deleteSection)
    public void delete(){
        mFolderDialog.delete(mFolder, getActivity(), getFragmentManager());

        mContentFragment.closePanel();
    }
}
