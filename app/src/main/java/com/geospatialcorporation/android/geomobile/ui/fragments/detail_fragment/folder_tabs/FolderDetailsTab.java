package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.FolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/8/2015.
 */
public class FolderDetailsTab extends GeoDetailsTabBase<Folder> {

    private static final String TAG = FolderDetailsTab.class.getSimpleName();

    FolderDetailsResponse mDetails;
    String mFolderType;
    @InjectView(R.id.fab) FloatingActionButton mFab;
    @InjectView(R.id.createdByValue) TextView mCreatedBy;
    @InjectView(R.id.createdValue) TextView mDateCreated;
    @InjectView(R.id.lastUpdatedValue) TextView mUpdated;
    @InjectView(R.id.userUpdateValue) TextView mUpdateUser;
    @InjectView(R.id.folderCountValue) TextView mFolderCount;
    @InjectView(R.id.entityCountValue) TextView mEntityCount;
    @InjectView(R.id.entityCountLabel) TextView mEntityCountLabel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_folder_details_tab, container, false);
        ButterKnife.inject(this, v);

        mFab.setOnClickListener(showActions);

        setIntentString(Folder.FOLDER_INTENT);

        Bundle args = getArguments();

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        mFolderType = args.getString("Folder Type");

        new GetFolderDetailsTask().execute();

        return v;
    }

    private View.OnClickListener showActions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GeoDialogHelper.folderActions(getActivity(), getActivity().getSupportFragmentManager());
        }
    };


    private class GetFolderDetailsTask extends AsyncTask<Void, Void, FolderDetailsResponse>{

        @Override
        protected FolderDetailsResponse doInBackground(Void... params) {
            mService = new FolderTreeService();

            if(mEntity != null){
                mDetails = ((FolderTreeService)mService).details(mEntity.getId());
            }

            return mDetails;
        }

        @Override
        protected void onPostExecute(FolderDetailsResponse response){
            mCreatedBy.setText(response.getCreateUser());
            mDateCreated.setText(response.getCreateDateTime());
            mUpdated.setText(response.getUpdateDateTime());
            mUpdateUser.setText(response.getUpdateUser());

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
}
