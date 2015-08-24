package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.MediaHelper;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.DocumentFolderDetailFragment;

import butterknife.OnClick;

public class LibraryFolderPanelFragment extends TreeFolderPanelFragmentBase {

    @Override
    protected int getViewResource() {
        return R.layout.fragment_panel_libraryfolder;
    }

    //OnClicks
    @OnClick(R.id.folderInfoSection)
    public void folderInfo(){
        Fragment f = new DocumentFolderDetailFragment();

        f.setArguments(mFolder.toBundle());

        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, f)
                .commit();
    }

    @OnClick(R.id.addDocumentSection)
    public void documentClicked(){
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("file/*");
        getActivity().startActivityForResult(chooseFileIntent, MainActivity.MediaConstants.PICK_FILE_REQUEST);
    }

    @OnClick(R.id.addFolderSection)
    public void folderClicked(){
        IFolderDialog folderDialog = application.getUIHelperComponent().provideFolderDialog();

        folderDialog.create(mFolder, getActivity(), getFragmentManager());
    }

    @OnClick(R.id.takePhotoSection)
    public void takePhoto(){
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
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        getActivity().startActivityForResult(chooserIntent, MainActivity.MediaConstants.PICK_IMAGE_REQUEST);
    }
}
