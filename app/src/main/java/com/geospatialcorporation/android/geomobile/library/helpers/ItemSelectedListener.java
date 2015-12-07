package com.geospatialcorporation.android.geomobile.library.helpers;

import android.view.View;
import android.widget.AdapterView;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.ISpinnerListener;

import java.util.List;

public class ItemSelectedListener<T> implements AdapterView.OnItemSelectedListener {

    List<T> mData;
    ISpinnerListener<T> mListener;



    public ItemSelectedListener(ISpinnerListener<T> listener) {
        mData = listener.getData();
        mListener = listener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            int entityPos = position - 1;

            mListener.setSelected(mData.get(entityPos));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
