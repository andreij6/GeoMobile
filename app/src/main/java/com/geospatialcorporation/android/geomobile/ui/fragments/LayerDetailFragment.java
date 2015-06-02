package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    Layer mLayer;

    @InjectView(R.id.layerNameTV) TextView mLayerName;
    @InjectView(R.id.deleteLayerIcon) ImageView mDeleteIcon;
    @InjectView(R.id.deleteLayerTV) TextView mDeleteText;
    @InjectView(R.id.backImageView) ImageView mBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.layer_detail_fragment, null);

        ButterKnife.inject(this, view);

        HandleArguments();

        SetupUI();

        return view;
    }

    @Override
    protected void HandleArguments() {
        Bundle args = getArguments();

        mLayer = args.getParcelable(Layer.LAYER_INTENT);
    }

    @Override
    protected void SetupUI(){
        mLayerName.setText(mLayer.getName());

        mDeleteIcon.setOnClickListener(DeleteonClickListner);
        mDeleteText.setOnClickListener(DeleteonClickListner);

        mBack.setOnClickListener(BackButtonClicked);
    }


    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            GeoDialogHelper.deleteLayer(getActivity(), mLayer, getFragmentManager());
        }
    };

}
