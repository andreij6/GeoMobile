package com.geospatialcorporation.android.geomobile.library.services.FolderDetailsCommon;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetFolderDetailsParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class FolderDetailsCommons implements IFolderDetailsCommons, IPostExecuter<FolderDetailsResponse> {

    IGetFolderDetailsTask mTask;
    IFolderDialog mFolderDialog;
    Folder mFolder;
    String mFolderType;
    ViewSetups mViews;

    public FolderDetailsCommons(){
        mTask = application.getTasksComponent().provideFolderDetailsTask();
        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();
    }

    @Override
    public IFolderDetailsCommons handleArguments(Bundle arguments) {
        mFolder = arguments.getParcelable(Folder.FOLDER_INTENT);

        mFolderType = arguments.getString("Folder Type");

        return this;
    }

    @Override
    public void getDataAsync() {
        mTask.getDetails(new GetFolderDetailsParams(mFolder, new FolderDetailsResponse(), this));
    }

    @Override
    public IFolderDetailsCommons setViews(ViewSetups views) {
        mViews =  views;
        return this;
    }

    @Override
    public void onPostExecute(FolderDetailsResponse response) {
        String emptyValues = "--";

        if(response == null){
            return;
        }

        if (response.getCreateUser() == null || response.getCreateUser().equals("")) {
            mViews.getCreatedBy().setText(emptyValues);
        } else {
            mViews.getCreatedBy().setText(response.getCreateUser());
        }

        mViews.getDateCreated().setText(DateTimeFormatter.format(response.getCreateDateTime()));

        if (response.getUpdateUser() != null && response.getUpdateUser().length() > 0) {
            mViews.getUpdatedUser().setText(response.getUpdateUser());
            mViews.getUpdated().setText(DateTimeFormatter.format(response.getUpdateDateTime()));
        } else {
            mViews.getUpdatedUser().setText(emptyValues);
            mViews.getUpdated().setText(emptyValues);
        }

        if(mFolder.getFolders() != null) {
            mViews.getFolderCount().setText(mFolder.getFolders().size() + "");
        } else {
            mViews.getFolderCount().setText("0");
        }

        if(mFolderType.equals("Layer")) {
            mViews.getEntityCountLabel().setText("Layer Count");
            if (mFolder.getLayers() != null) {
                mViews.getEntityCount().setText(mFolder.getLayers().size() + "");
            } else {
                mViews.getEntityCount().setText("0");
            }
        } else {
            mViews.getEntityCountLabel().setText("Document Count");
            if (mFolder.getDocuments() != null) {
                mViews.getEntityCount().setText(mFolder.getDocuments().size() + "");
            } else {
                mViews.getEntityCount().setText("0");
            }
        }
    }

    public static class ViewSetups {
        TextView mCreatedBy;
        TextView mDateCreated;
        TextView mUpdated;
        TextView mUpdatedUser;
        TextView mFolderCount;
        TextView mEntityCount;
        TextView mEntityCountLabel;

        public ViewSetups(TextView createdBy, TextView dateCreated, TextView updated, TextView updateUser, TextView folderCount, TextView entityCount, TextView entityCountLabel) {
            mCreatedBy = createdBy;
            mDateCreated = dateCreated;
            mUpdated = updated;
            mUpdatedUser = updateUser;
            mFolderCount = folderCount;
            mEntityCount = entityCount;
            mEntityCountLabel = entityCountLabel;
        }


        //region Setters
        public void setCreatedBy(TextView createdBy) {
            mCreatedBy = createdBy;
        }

        public void setDateCreated(TextView dateCreated) {
            mDateCreated = dateCreated;
        }

        public void setUpdated(TextView updated) {
            mUpdated = updated;
        }

        public void setUpdatedUser(TextView updatedUser) {
            mUpdatedUser = updatedUser;
        }

        public void setFolderCount(TextView folderCount) {
            mFolderCount = folderCount;
        }

        public void setEntityCount(TextView entityCount) {
            mEntityCount = entityCount;
        }

        public void setEntityCountLabel(TextView entityCountLabel) {
            mEntityCountLabel = entityCountLabel;
        }
        //endregion

        //region Getters
        public TextView getCreatedBy() {
            return mCreatedBy;
        }

        public TextView getDateCreated() {
            return mDateCreated;
        }

        public TextView getUpdated() {
            return mUpdated;
        }

        public TextView getUpdatedUser() {
            return mUpdatedUser;
        }

        public TextView getFolderCount() {
            return mFolderCount;
        }

        public TextView getEntityCount() {
            return mEntityCount;
        }

        public TextView getEntityCountLabel() {
            return mEntityCountLabel;
        }
        //endregion
    }
}
