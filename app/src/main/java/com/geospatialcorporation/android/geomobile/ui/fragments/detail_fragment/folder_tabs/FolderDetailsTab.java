package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetFolderDetailsParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.DateTimeFormatter;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FolderDetailsTab extends GeoDetailsTabBase<Folder> implements IPostExecuter<FolderDetailsResponse> {

    private static final String TAG = FolderDetailsTab.class.getSimpleName();

    //region Properties & Butterknife
    IGetFolderDetailsTask mTask;
    IFolderDialog mFolderDialog;
    FolderDetailsResponse mDetails;
    String mFolderType;
    @Bind(R.id.createdByValue) TextView mCreatedByValue;
    @Bind(R.id.createdValue) TextView mDateCreatedValue;
    @Bind(R.id.lastUpdatedValue) TextView mUpdatedValue;
    @Bind(R.id.userUpdateValue) TextView mUpdateUserValue;
    @Bind(R.id.folderCountValue) TextView mFolderCountValue;
    @Bind(R.id.entityCountValue) TextView mEntityCountValue;
    @Bind(R.id.entityCountLabel) TextView mEntityCountLabel;
    @Bind(R.id.updateUserLabel) TextView mUpdateUserLabel;
    @Bind(R.id.lastUpdatedLabel) TextView mLastUpdateLabel;
    //endregion


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_folder_details_tab, container, false);
        ButterKnife.bind(this, v);

        setIntentString(Folder.FOLDER_INTENT);

        Bundle args = getArguments();

        mTask = application.getTasksComponent().provideFolderDetailsTask();
        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().FolderDetailTab());

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        mFolderType = args.getString(Folder.FOLDER_TYPE_INTENT);

        refresh();

        return v;
    }

    @Override
    public void refresh() {
        mTask.getDetails(new GetFolderDetailsParams(mEntity, mDetails, this));
    }

    @Override
    public void onPostExecute(FolderDetailsResponse response){
        String emptyValue = "--";

        if(response == null){
            return;
        }

        try {
            if (response.getCreateUser() == null || response.getCreateUser().equals("")) {
                mCreatedByValue.setText(emptyValue);
            } else {
                mCreatedByValue.setText(response.getCreateUser());
            }

            mDateCreatedValue.setText(DateTimeFormatter.format(response.getCreateDateTime()));

            if (response.getUpdateUser() != null && response.getUpdateUser().length() > 0 && !response.getUpdateUser().equals("")) {
                mUpdateUserValue.setText(response.getUpdateUser());
                mUpdatedValue.setText(DateTimeFormatter.format(response.getUpdateDateTime()));
            } else {
                mUpdateUserValue.setText(emptyValue);
                mUpdatedValue.setText(emptyValue);
            }

            if (mEntity.getFolders() != null) {
                mFolderCountValue.setText(mEntity.getFolders().size() + "");
            } else {
                mFolderCountValue.setText("0");
            }

            if (mFolderType.equals("Layer")) {
                mEntityCountLabel.setText("Layer Count");
                if (mEntity.getLayers() != null) {
                    mEntityCountValue.setText(mEntity.getLayers().size() + "");
                } else {
                    mEntityCountValue.setText("0");
                }
            } else {
                mEntityCountLabel.setText("Document Count");
                if (mEntity.getDocuments() != null) {
                    mEntityCountValue.setText(mEntity.getDocuments().size() + "");
                } else {
                    mEntityCountValue.setText("0");
                }
            }

        } catch (NullPointerException e){
            mAnalytics.sendException(e);
        } catch (Exception e){
            mAnalytics.sendException(e);
        }
    }

}
