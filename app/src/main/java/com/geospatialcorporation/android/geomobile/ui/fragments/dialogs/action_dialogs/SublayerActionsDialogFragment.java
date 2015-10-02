package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ISublayerDialog;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SublayerActionsDialogFragment extends GeoDialogFragmentBase {

    Layer mSublayer;
    //ISlidingPanelManager mPanelManager;
    ISublayerDialog mSublayerDialog;

    public void init(Context context, Layer layer){
       setContext(context);
        mSublayer = layer;
        //mPanelManager = new PanelManager(GeoPanel.SUBLAYER);
        //mPanelManager.setup();
    }

    //region ButterKnife
    @Bind(R.id.styleIV)ImageView mStyleIV;
    @Bind(R.id.styleTV)TextView mStyleTV;
    @Bind(R.id.renameTV) TextView mRename;
    @Bind(R.id.renameIV) ImageView mRenameIV;
    @Bind(R.id.filtersIV) ImageView mFilterIV;
    @Bind(R.id.filterTV) TextView mFilterTV;
    @Bind(R.id.deleteIV) ImageView mDeleteIV;
    @Bind(R.id.deleteTV) TextView mDeleteTV;
    //endregion

    @Override
    public void onStart(){
        super.onStart();

        AlertDialog d = (AlertDialog)getDialog();

        if(d != null){
            Button negative = d.getButton(Dialog.BUTTON_NEGATIVE);
            negative.setEnabled(false);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_actions_sublayers);

        ButterKnife.bind(this, v);

        mSublayerDialog = application.getUIHelperComponent().provideSublayerDialog();

        mStyleIV.setOnClickListener(ModifyStyle);
        mStyleTV.setOnClickListener(ModifyStyle);

        mDeleteIV.setOnClickListener(DeleteClicked);
        mDeleteTV.setOnClickListener(DeleteClicked);

        mFilterIV.setOnClickListener(EditFilter);
        mFilterTV.setOnClickListener(EditFilter);

        mRename.setOnClickListener(RenameClicked);
        mRenameIV.setOnClickListener(RenameClicked);

        builder.setTitle(R.string.edit_sublayer)
                .setView(v)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    //region OnClicks
    protected View.OnClickListener ModifyStyle = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            //mPanelManager.anchor();
            SublayerActionsDialogFragment.this.getDialog().cancel();
        }
    };

    protected  View.OnClickListener EditFilter = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            //mPanelManager.anchor();
            SublayerActionsDialogFragment.this.getDialog().cancel();
        }
    };

    protected  View.OnClickListener RenameClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            mSublayerDialog.rename(mSublayer, getContext(), getFragmentManager());
            SublayerActionsDialogFragment.this.getDialog().cancel();
        }
    };

    protected  View.OnClickListener DeleteClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            mSublayerDialog.delete(mSublayer, getContext(), getFragmentManager());
            SublayerActionsDialogFragment.this.getDialog().cancel();
        }
    };
    //endregion
}
