package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/2/2015.
 */
public class FolderDetailFragment extends ItemDetailFragment<Folder> {
    private static final String TAG = FolderDetailFragment.class.getSimpleName();
    
    @InjectView(R.id.folderNameTV) TextView mFolderName;
    @InjectView(R.id.deleteFolderIcon) ImageView mDeleteIcon;
    @InjectView(R.id.deleteFolderTV) TextView mDeleteText;
    @InjectView(R.id.backImageView) ImageView mBack;
    @InjectView(R.id.folderNameET) EditText mNameET;
    @InjectView(R.id.saveBtn) Button mSave;
    @InjectView(R.id.editBtn)
    ImageButton mEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_folder, null);

        ButterKnife.inject(this, view);

        HandleArguments();

        SetupUI();

        return view;
    }

    @Override
    protected void HandleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

    }

    @Override
    protected void SetupUI(){
        mFolderName.setText(mEntity.getName());
        SetTitle(mEntity.getName());
        mDeleteIcon.setOnClickListener(DeleteonClickListner);
        mDeleteText.setOnClickListener(DeleteonClickListner);

        mBack.setOnClickListener(BackButtonClicked);

        mNameET.setOnClickListener(EditNameClicked);
        mEdit.setOnClickListener(EditNameClicked);

        setButton(mSave);
        setEditText(mNameET);
        SetupRename();
    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            GeoDialogHelper.deleteFolder(getActivity(), mEntity, getFragmentManager());
        }
    };

}
