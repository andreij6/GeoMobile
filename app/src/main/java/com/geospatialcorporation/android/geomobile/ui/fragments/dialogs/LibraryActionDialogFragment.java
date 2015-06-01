package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;

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
    //endregion

    //region Properties
    Context mContext;
    @InjectView(R.id.addLibraryFolderTV) TextView mFolder;
    @InjectView(R.id.addLibraryFolderIcon) ImageView mFolderImageView;
    @InjectView(R.id.addDocumentIcon) ImageView mDocumentImageView;
    @InjectView(R.id.addDocumentTV) TextView mDocumentTextView;
    @InjectView(R.id.addDocumentSection) LinearLayout mDocumentSection;
    @InjectView(R.id.addFolderSection) LinearLayout mFolderSection;
    @InjectView(R.id.addImageSection) LinearLayout mImageSection;
    //endregion

    //region OnClicks
    @OnClick(R.id.addDocumentSection)
    public void documentClicked(){
        highlight(mDocumentSection);

        unhighlight(mFolderSection);
        unhighlight(mImageSection);
    }

    @OnClick(R.id.addFolderSection)
    public void folderClicked(){
        highlight(mFolderSection);

        unhighlight(mImageSection);
        unhighlight(mDocumentSection);
    }

    @OnClick(R.id.addImageSection)
    public void imageClicked(){
        highlight(mImageSection);

        unhighlight(mFolderSection);
        unhighlight(mDocumentSection);
    }

    private void highlight(LinearLayout layout){
        layout.setBackgroundColor(Color.LTGRAY);
    }

    private void unhighlight(LinearLayout layout){
        layout.setBackgroundColor(Color.WHITE);
    }
    //endregion

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = inflater.inflate(R.layout.dialog_library, null);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.library_dialog_title);
        builder.setView(v);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LibraryActionDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();

    }
}
