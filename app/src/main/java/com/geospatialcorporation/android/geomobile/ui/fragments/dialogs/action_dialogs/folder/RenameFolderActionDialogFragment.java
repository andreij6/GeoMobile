package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RenameFolderActionDialogFragment extends FolderActionDialogBase {
    @Bind(R.id.renameInput)
    EditText mRenameInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        if(savedInstanceState != null){
            mFolder = savedInstanceState.getParcelable(FOLDER_DIALOG);
            init(mFolder);
        }

        View v = getDialogView(R.layout.dialog_shared_rename);
        ButterKnife.bind(this, v);

        mRenameInput.setText(mFolder.getName());
        showKeyboard(mRenameInput);

        return getDialogBuilder()
                .setTitle(R.string.rename)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = mRenameInput.getText().toString();

                        if (!newName.isEmpty()) {
                            Toaster(mContext.getString(R.string.rename_request_sent));
                            mService.rename(mFolder.getId(), newName);
                            getFragmentManager().popBackStack();
                            hideKeyBoard(mRenameInput);
                        } else {
                            Toaster(mContext.getString(R.string.folder_rename_validation));
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideKeyBoard(mRenameInput);
                        dialog.cancel();
                    }
                }).create();
    }


}
