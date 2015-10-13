package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.constants.MediaConstants;
import com.geospatialcorporation.android.geomobile.library.helpers.MediaHelper;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet.TabDocumentFolderDetailFragment;

import butterknife.OnClick;

public class TabletLibraryFolderPanelFragment extends TabletTreeFolderPanelFragmentBase {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_panel_libraryfolder;
    }

    //OnClicks
    //@OnClick(R.id.folderInfoSection)
    //public void folderInfo(){
    //    Fragment f = new TabDocumentFolderDetailFragment();
    //
    //    f.setArguments(mFolder.toBundle());
    //
    //    getFragmentManager()
    //            .beginTransaction()
    //            .addToBackStack(null)
    //            .replace(R.id.info_frame, f)
    //            .commit();
    //}

    //TODO: works but onSuccess doesnt work
    @OnClick(R.id.addFolderSection)
    public void folderClicked(){
        mPanelCtrl.closePanel();

        IFolderDialog folderDialog = application.getUIHelperComponent().provideFolderDialog();

        folderDialog.create(mFolder, getActivity(), getFragmentManager());
    }

    //TODO: implement onActivity Response on TableLibraryFragment
    @OnClick(R.id.addDocumentSection)
    public void documentClicked(){
        mPanelCtrl.closePanel();

        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("file/*");
        getActivity().startActivityForResult(chooseFileIntent, MediaConstants.PICK_FILE_REQUEST);
    }

    //TODO: might work
    @OnClick(R.id.takePhotoSection)
    public void takePhoto(){
        mPanelCtrl.closePanel();

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        MediaHelper mediaHelper = new MediaHelper(getActivity());

        application.mMediaUri = mediaHelper.getOutputMediaFileUri(MediaConstants.MEDIA_TYPE_IMAGE);

        if (application.mMediaUri == null) {
            // display an error
            Toast.makeText(getActivity(), R.string.error_external_storage, Toast.LENGTH_LONG).show();
        }
        else {
            mAnalytics.trackClick(new GoogleAnalyticEvent().UploadImage());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, application.mMediaUri);
            getActivity().startActivityForResult(takePhotoIntent, MediaConstants.TAKE_IMAGE_REQUEST);
        }
    }

    //TODO: might work
    @OnClick(R.id.choosePhotoSection)
    public void uploadImage(){
        mPanelCtrl.closePanel();

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        getActivity().startActivityForResult(chooserIntent, MediaConstants.PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.close)
    public void close(){
        mPanelCtrl.closePanel();
    }

}
