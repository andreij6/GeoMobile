package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.R;

/**
 * Created by andre on 6/2/2015.
 */
public class DeleteDocumentDialogFragment extends DocumentActionDialogBase {
    private static final String TAG = DeleteDocumentDialogFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return getDialogBuilder()
                .setTitle(R.string.are_you_sure_title)
                .setMessage(getString(R.string.are_you_sure) + " " + mDocument.getNameWithExt())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Service.delete(mDocument);
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }
}
