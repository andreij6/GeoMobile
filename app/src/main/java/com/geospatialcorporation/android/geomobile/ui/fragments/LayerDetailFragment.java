package com.geospatialcorporation.android.geomobile.ui.fragments;

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
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/2/2015.
 */
public class LayerDetailFragment extends ItemDetailFragment<Layer> {
    private static final String TAG = LayerDetailFragment.class.getSimpleName();

    @InjectView(R.id.layerNameTV) TextView mLayerName;
    @InjectView(R.id.deleteLayerIcon) ImageView mDeleteIcon;
    @InjectView(R.id.deleteLayerTV) TextView mDeleteText;
    @InjectView(R.id.backImageView) ImageView mBack;
    @InjectView(R.id.layerNameET) EditText mNameET;
    @InjectView(R.id.saveBtn) Button mSave;
    @InjectView(R.id.editBtn) ImageButton mEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_detail_layer, null);

        ButterKnife.inject(this, view);

        HandleArguments();

        SetupUI();

        return view;
    }

    @Override
    protected void HandleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Layer.LAYER_INTENT);

        SetTitle(mEntity.getName());
    }

    @Override
    protected void SetupUI(){
        mLayerName.setText(mEntity.getName());

        mDeleteIcon.setOnClickListener(DeleteonClickListner);
        mDeleteText.setOnClickListener(DeleteonClickListner);

        mBack.setOnClickListener(BackButtonClicked);

        mEdit.setOnClickListener(EditNameClicked);
        mNameET.setOnClickListener(EditNameClicked);

        setButton(mSave);
        setEditText(mNameET);
        SetupRename();
    }


    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            GeoDialogHelper.deleteLayer(getActivity(), mEntity, getFragmentManager());
        }
    };

}
