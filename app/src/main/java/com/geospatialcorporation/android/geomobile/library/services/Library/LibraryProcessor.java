package com.geospatialcorporation.android.geomobile.library.services.Library;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LibraryFolderPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public class LibraryProcessor implements ILibraryProcessor {

    LibraryProcessorProps mProps;

    public LibraryProcessor(LibraryProcessorProps props) {
        mProps = props;
    }

    @Override
    public void handleArgs(Bundle args, GetDocumentsParam params) {
        mProps.getProgressDialog().showProgressDialog();

        if (args != null) {
            int folderId = args.getInt(Folder.FOLDER_INTENT, 0);

            mProps.getDocumentsTask().getDocumentsByFolderId(params, folderId);
        } else {
            mProps.getDocumentsTask().getDocumentsByFolderId(params, 0);
        }
    }

    @Override
    public void hideProgressDialog() {
        mProps.getProgressDialog().hideProgressDialog();
    }

    @Override
    public List<ListItem> getListItemData(Folder currentFolder) {
        DataHelper dataHelper = new DataHelper();

        return dataHelper.CombineLibraryItems(currentFolder.getDocuments(), currentFolder.getFolders(), currentFolder.getParent());
    }

    @Override
    public ISlidingPanelManager getPanelManager() {
        return mProps.getPanelManager();
    }

    @Override
    public void onOptionsButtonPressed(Folder currentFolder, FragmentManager fragmentManager, Fragment fragment) {
        ISlidingPanelManager panelManager = mProps.getPanelManager();

        if(!panelManager.getIsOpen()){

            fragment.setArguments(currentFolder.toBundle());

            fragmentManager.beginTransaction()
                    .replace(R.id.slider_content, fragment)
                    .commit();

            panelManager.halfAnchor(0.15f);
            panelManager.touch(false);
        } else {
            panelManager.hide();
        }
    }

    @Override
    public void closePanel() {
        mProps.getPanelManager().hide();
    }

    @Override
    public IDocumentTreeService getDocumentUploader() {
        return application.getTreeServiceComponent().provideDocumentTreeService();
    }

    public static class Builder {

        LibraryProcessorProps mProps;

        public Builder(){
            mProps = new LibraryProcessorProps();
        }

        public Builder progressDialog(Context context) {
            ProgressDialogHelper progressDialogHelper = new ProgressDialogHelper(context);
            mProps.setProgressDiaglog(progressDialogHelper);
            return this;
        }

        public Builder panel(SlidingUpPanelLayout panel) {
            application.setLibraryFragmentPanel(panel);
            ISlidingPanelManager panelManager = new PanelManager.Builder().type(GeoPanel.LIBRARY_FRAGMENT).hide().build();
            mProps.setPanelManager(panelManager);
            return this;
        }

        public Builder swipeRefresh(SwipeRefreshLayout swipeRefreshLayout, IContentRefresher contentRefresher, Context context) {
            ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();
            swipeRefreshLayout.setOnRefreshListener(refresher.build(swipeRefreshLayout, contentRefresher));
            swipeRefreshLayout.setProgressBackgroundColorSchemeColor(context.getResources().getColor(R.color.white));

            return this;
        }

        public ILibraryProcessor build() {
            return new LibraryProcessor(mProps);
        }
    }

    protected static class LibraryProcessorProps {

        public LibraryProcessorProps(){
            mGetDocumentsTask = application.getTasksComponent().provideGetDocumentsTask();
        }

        private ProgressDialogHelper mProgressDialog;
        private ISlidingPanelManager mPanelManager;
        private IGetDocumentsTask mGetDocumentsTask;

        public void setProgressDiaglog(ProgressDialogHelper progressDialog) {
            mProgressDialog = progressDialog;
        }

        public void setPanelManager(ISlidingPanelManager panelManager) {
            mPanelManager = panelManager;
        }

        public IGetDocumentsTask getDocumentsTask() {
            return mGetDocumentsTask;
        }

        public ProgressDialogHelper getProgressDialog() {
            return mProgressDialog;
        }

        public ISlidingPanelManager getPanelManager() {
            return mPanelManager;
        }
    }
}
