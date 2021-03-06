package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.MediaHelper;
import com.geospatialcorporation.android.geomobile.ui.activity.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;

import butterknife.OnClick;

public class LibraryFolderPanelFragment extends TreeFolderPanelFragmentBase<LibraryFragment> {

    @Override
    protected int getViewResource() {
        return R.layout.fragment_panel_libraryfolder;
    }

    //OnClicks
    @OnClick(R.id.addDocumentSection)
    public void documentClicked(){
        mContentFragment.closePanel();

        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("file/*");
        getActivity().startActivityForResult(chooseFileIntent, MainActivity.MediaConstants.PICK_FILE_REQUEST);
    }

    @OnClick(R.id.addFolderSection)
    public void folderClicked(){
        mContentFragment.closePanel();

        IFolderDialog folderDialog = application.getUIHelperComponent().provideFolderDialog();

        folderDialog.create(mFolder, getActivity(), getFragmentManager());
    }

    @OnClick(R.id.takePhotoSection)
    public void takePhoto(){
        mContentFragment.closePanel();

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        MediaHelper mediaHelper = new MediaHelper(getActivity());

        application.mMediaUri = mediaHelper.getOutputMediaFileUri(MainActivity.MediaConstants.MEDIA_TYPE_IMAGE);

        if (application.mMediaUri == null) {
            // display an error
            Toast.makeText(getActivity(), R.string.error_external_storage, Toast.LENGTH_LONG).show();
        }
        else {
            mAnalytics.trackClick(new GoogleAnalyticEvent().UploadImage());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, application.mMediaUri);
            getActivity().startActivityForResult(takePhotoIntent, MainActivity.MediaConstants.TAKE_IMAGE_REQUEST);
        }
    }

    @OnClick(R.id.choosePhotoSection)
    public void uploadImage(){
        mContentFragment.closePanel();

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        getActivity().startActivityForResult(chooserIntent, MainActivity.MediaConstants.PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.close)
    public void close(){
        mContentFragment.closePanel();
    }
}
