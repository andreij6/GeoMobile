package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.rest.DocumentService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/2/2015.
 */
public class UploadImageDialogFragment extends GeoUploadDialogFragmentBase {

    //region Inject
    @InjectView(R.id.takeSection) LinearLayout mTake;
    @InjectView(R.id.selectSection) LinearLayout mSelect;
    //endregion

    //region OnClick
    @SuppressWarnings("unused")
    @OnClick(R.id.takeSection)
    public void takeSectionClicked(){
        highlight(mTake);
        unhighlight(mSelect);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.selectSection)
    public void selectSectionClicked(){
        highlight(mSelect);
        unhighlight(mTake);
    }
    //endregion

    private void highlight(LinearLayout layout){
        layout.setBackgroundColor(Color.LTGRAY);
    }

    private void unhighlight(LinearLayout layout){
        layout.setBackgroundColor(Color.WHITE);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        final View v = getDialogView(R.layout.dialog_image_upload);
        ButterKnife.inject(this, v);

        TextView takeTV = (TextView)v.findViewById(R.id.takeTV);
        ImageView takeIV = (ImageView)v.findViewById(R.id.takeIV);
        TextView uploadTV = (TextView)v.findViewById(R.id.uploadTV);
        ImageView uploadIV = (ImageView)v.findViewById(R.id.uploadIV);

        return getDialogBuilder()
                .setTitle(R.string.upload_image)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    LinearLayout t = (LinearLayout)v.findViewById(R.id.takeSection);
                    LinearLayout s = (LinearLayout)v.findViewById(R.id.selectSection);

                    if (isHighlighted(t)) {
                        Intent chooseImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(chooseImage, MainActivity.MediaConstants.PICK_IMAGE_REQUEST);
                    }

                    if(isHighlighted(s)){
                        Intent takeImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getActivity().startActivityForResult(takeImage, MainActivity.MediaConstants.TAKE_IMAGE_REQUEST);
                    }

                    dialog.cancel();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }

    private boolean isHighlighted(LinearLayout d) {
        Integer b = ((ColorDrawable)d.getBackground()).getColor();

        return (b == Color.WHITE);
    }

}
