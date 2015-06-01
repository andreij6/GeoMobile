package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LayerActionDialogFragment extends DialogFragment {
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
    @InjectView(R.id.addLayerFolderSection) LinearLayout mFolderSection;
    @InjectView(R.id.addLayerSection) LinearLayout mLayerSection;
    //endregion

    //region OnClick
    @OnClick(R.id.addLayerSection)
    public void layerSectionClicked(){
        highlight(mLayerSection);
        unhighlight(mFolderSection);
    }

    @OnClick(R.id.addLayerFolderSection)
    public void folderSectionClicked(){
        highlight(mFolderSection);
        unhighlight(mLayerSection);
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

        View v = inflater.inflate(R.layout.dialog_layer, null);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.layer_dialog_title);
        builder.setView(v);

        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LayerActionDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().getWindow().setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        p.width = size.x;
        p.y = 200;

        getDialog().getWindow().setAttributes(p);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
    **/
}
