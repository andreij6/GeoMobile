package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DocumentActionsDialogFragment extends GeoDialogFragmentBase {

    private static final String TAG = DocumentActionsDialogFragment.class.getSimpleName();

    Document mDocument;
    IDocumentDialog mDocumentDialog;

    public void init(Context context, Document document){
        setContext(context);
        mDocument = document;
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
    @InjectView(R.id.renameSection) LinearLayout mRename;
    @InjectView(R.id.deleteSection) LinearLayout mDelete;
    @InjectView(R.id.moveSection) LinearLayout mMove;

    @OnClick(R.id.renameSection)
    public void rename(){
        mDocumentDialog.rename(mDocument, getContext(), getFragmentManager());
        DocumentActionsDialogFragment.this.getDialog().cancel();
    }

    @OnClick(R.id.deleteSection)
    public void delete(){
        mDocumentDialog.delete(mDocument, getContext(), getFragmentManager());
        DocumentActionsDialogFragment.this.getDialog().cancel();
    }

    @OnClick(R.id.moveSection)
    public void move(){
       mDocumentDialog.move(mDocument, getContext(), getFragmentManager());
        DocumentActionsDialogFragment.this.getDialog().cancel();

    }
    //endregion

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_actions_document);
        ButterKnife.inject(this, v);

        mDocumentDialog = application.getUIHelperComponent().provideDocumentDialog();

        builder.setTitle(R.string.document_actions).setView(v).setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
