package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.MediaHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LibraryActionDialogFragment extends DialogFragment {

    //region Getters & Setters
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
    public Folder getFolder() {
        return mFolder;
    }

    public void setFolder(Folder folder) {
        mFolder = folder;
    }
    //endregion

    //region Properties
    IGeoAnalytics mAnalytics;
    Context mContext;
    Folder mFolder;
    IDocumentDialog mDocumentDialog;
    IFolderDialog mFolderDialog;
    @InjectView(R.id.addLibraryFolderTV) TextView mFolderTextView;
    @InjectView(R.id.addLibraryFolderIcon) ImageView mFolderImageView;
    @InjectView(R.id.addDocumentIcon) ImageView mDocumentImageView;
    @InjectView(R.id.addDocumentTV) TextView mDocumentTextView;
    //endregion

    //region OnClicks
    @OnClick(R.id.addLibraryFolderTV)
    public void documentClicked(){
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("file/*");
        getActivity().startActivityForResult(chooseFileIntent, MainActivity.MediaConstants.PICK_FILE_REQUEST);

        LibraryActionDialogFragment.this.getDialog().cancel();
    }

    @OnClick(R.id.addDocumentIcon)
    public void folderClicked(){
        mFolderDialog.create(mFolder, mContext, getFragmentManager());

        LibraryActionDialogFragment.this.getDialog().cancel();

    }

    @OnClick(R.id.takePhotoIcon)
    public void takePhoto(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        MediaHelper mediaHelper = new MediaHelper(mContext);

        application.mMediaUri = mediaHelper.getOutputMediaFileUri(MainActivity.MediaConstants.MEDIA_TYPE_IMAGE);

        if (application.mMediaUri == null) {
            // display an error
            Toast.makeText(mContext, R.string.error_external_storage, Toast.LENGTH_LONG).show();
        }
        else {
            mAnalytics.trackClick(new GoogleAnalyticEvent().UploadImage());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, application.mMediaUri);
            getActivity().startActivityForResult(takePhotoIntent, MainActivity.MediaConstants.TAKE_IMAGE_REQUEST);

            LibraryActionDialogFragment.this.getDialog().cancel();
        }


    }

    @OnClick(R.id.uploadIV)
    public void uploadImage(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        getActivity().startActivityForResult(chooserIntent, MainActivity.MediaConstants.PICK_IMAGE_REQUEST);

        LibraryActionDialogFragment.this.getDialog().cancel();
    }
    //endregion

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();
        mDocumentDialog = application.getUIHelperComponent().provideDocumentDialog();

        final View v = inflater.inflate(R.layout.dialog_actions_library, null);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.library_dialog_title);
        builder.setView(v);

        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();

    }
}
