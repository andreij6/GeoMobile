package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.FolderTreeService;

/**
 * Created by andre on 6/1/2015.
 */
public class CreateFolderDialogFragment extends CreateDialogFragmentBase {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();
        View v = getDialogView(R.layout.dialog_create_folder);
        final FolderTreeService service = new FolderTreeService();

        final EditText name = (EditText)v.findViewById(R.id.folderNameInput);

        builder.setTitle(R.string.create_folder);

        builder.setView(v);

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Validation
                service.createFolder(name.getText().toString(), mFolder.getId());
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CreateFolderDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
