package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RenameDocumentDialogFragment extends DocumentActionDialogBase {

    @Bind(R.id.renameInput) EditText mRenameInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        if(savedInstanceState != null){
            mDocument = savedInstanceState.getParcelable(DOCUMENT_DIALOG);
            init(mDocument);
        }

        View v = getDialogView(R.layout.dialog_shared_rename);
        ButterKnife.bind(this, v);

        mRenameInput.setText(mDocument.getName());
        showKeyboard(mRenameInput);

        return getDialogBuilder()
                .setTitle(R.string.rename)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = mRenameInput.getText().toString();

                        if (!newName.isEmpty()) {
                            Service.rename(mDocument.getId(), newName);
                            getFragmentManager().popBackStack();
                        } else {
                            Toaster("Please add a valid Name");
                        }
                        hideKeyBoard(mRenameInput);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        hideKeyBoard(mRenameInput);
                    }
                }).create();
    }

}
