package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/2/2015.
 */
public class FolderDetailFragment extends ItemDetailFragment<Folder> {
    private static final String TAG = FolderDetailFragment.class.getSimpleName();

    Folder mFolder;
    
    @InjectView(R.id.folderNameTV) TextView mFolderName;
    @InjectView(R.id.deleteFolderIcon) ImageView mDeleteIcon;
    @InjectView(R.id.deleteFolderTV) TextView mDeleteText;
    @InjectView(R.id.backImageView) ImageView mBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.folder_detail_fragment, null);

        ButterKnife.inject(this, view);

        HandleArguments();

        SetupUI();

        return view;
    }

    @Override
    protected void HandleArguments() {
        Bundle args = getArguments();

        mFolder = args.getParcelable(Folder.FOLDER_INTENT);
    }

    @Override
    protected void SetupUI(){
        mFolderName.setText(mFolder.getName());

        mDeleteIcon.setOnClickListener(DeleteonClickListner);
        mDeleteIcon.setOnClickListener(DeleteonClickListner);

        mBack.setOnClickListener(BackButtonClicked);
    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Toast.makeText(getActivity(), "Delete Not Implemented", Toast.LENGTH_LONG).show();
        }
    };

}
