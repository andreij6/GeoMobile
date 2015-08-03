package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LayerActionsDialogFragment extends GeoDialogFragmentBase {
    Layer mLayer;
    ILayerDialog mLayerDialog;

    public void init(Context context, Layer layer){
        setContext(context);
        mLayer = layer;
    }

    @Override
    public void onStart(){
        super.onStart();

        AlertDialog d = (AlertDialog)getDialog();

        if(d != null){
            Button negative = d.getButton(Dialog.BUTTON_NEGATIVE);
            negative.setEnabled(false);
        }
    }

    //region ButterKnife
    @InjectView(R.id.styleIV) ImageView mStyleIV;
    @InjectView(R.id.renameIV) ImageView mRenameIV;
    @InjectView(R.id.deleteIV) ImageView mDeleteIV;
    @InjectView(R.id.styleTV) TextView mStyleTV;
    @InjectView(R.id.deleteTV) TextView mDeleteTV;
    @InjectView(R.id.renameTV) TextView mRenameTV;
    //endregion

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_actions_layer);

        ButterKnife.inject(this, v);

        mLayerDialog = application.getUIHelperComponent().provideLayerDialog();

        mStyleIV.setOnClickListener(styleLayer);
        mStyleTV.setOnClickListener(styleLayer);

        mRenameIV.setOnClickListener(renameLayer);
        mRenameTV.setOnClickListener(renameLayer);

        mDeleteIV.setOnClickListener(deleteLayer);
        mDeleteTV.setOnClickListener(deleteLayer);

        builder.setTitle(R.string.layer_actions)
                .setView(v)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    protected View.OnClickListener renameLayer = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            mLayerDialog.rename(mLayer, getContext(), getFragmentManager());
            LayerActionsDialogFragment.this.getDialog().cancel();
        }
    };

    protected View.OnClickListener styleLayer = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            Toaster("Style Layer");
        }
    };

    protected View.OnClickListener deleteLayer = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            mLayerDialog.delete(mLayer, getContext(), getFragmentManager());
            LayerActionsDialogFragment.this.getDialog().cancel();


        }
    };
}
