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
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FolderDetailsTab extends GeoDetailsTabBase<Folder> implements IPostExecuter<FolderDetailsResponse> {

    private static final String TAG = FolderDetailsTab.class.getSimpleName();

    //region Properties & Butterknife
    IGetFolderDetailsTask mTask;
    IFolderDialog mFolderDialog;
    FolderDetailsResponse mDetails;
    String mFolderType;
    @InjectView(R.id.createdByValue) TextView mCreatedBy;
    @InjectView(R.id.createdValue) TextView mDateCreated;
    @InjectView(R.id.lastUpdatedValue) TextView mUpdated;
    @InjectView(R.id.userUpdateValue) TextView mUpdateUser;
    @InjectView(R.id.folderCountValue) TextView mFolderCount;
    @InjectView(R.id.entityCountValue) TextView mEntityCount;
    @InjectView(R.id.entityCountLabel) TextView mEntityCountLabel;
    //endregion


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_folder_details_tab, container, false);
        ButterKnife.inject(this, v);

        setIntentString(Folder.FOLDER_INTENT);

        Bundle args = getArguments();

        mTask = application.getTasksComponent().provideFolderDetailsTask();
        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().FolderDetailTab());

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        mFolderType = args.getString("Folder Type");

        refresh();

        return v;
    }

    @Override
    public void refresh() {
        mTask.getDetails(new GetFolderDetailsParams(mEntity, mDetails, this));
    }

    @Override
    public void onPostExecute(FolderDetailsResponse response){
        mCreatedBy.setText(response.getCreateUser());
        mDateCreated.setText(DateTimeFormatter.format(response.getCreateDateTime()));

        if (response.getUpdateUser().length() > 0) {
            mUpdateUser.setText(response.getUpdateUser());
            mUpdated.setText(DateTimeFormatter.format(response.getUpdateDateTime()));
        } else {
            mUpdateUser.setVisibility(View.GONE);
            mUpdated.setVisibility(View.GONE);
        }

        if(mEntity.getFolders() != null) {
            mFolderCount.setText(mEntity.getFolders().size() + "");
        } else {
            mFolderCount.setText("0");
        }

        if(mFolderType.equals("Layer")) {
            mEntityCountLabel.setText("Layer Count");
            if (mEntity.getLayers() != null) {
                mEntityCount.setText(mEntity.getLayers().size() + "");
            } else {
                mEntityCount.setText("0");
            }
        } else {
            mEntityCountLabel.setText("Document Count");
            if (mEntity.getDocuments() != null) {
                mEntityCount.setText(mEntity.getDocuments().size() + "");
            } else {
                mEntityCount.setText("0");
            }
        }
    }

}
