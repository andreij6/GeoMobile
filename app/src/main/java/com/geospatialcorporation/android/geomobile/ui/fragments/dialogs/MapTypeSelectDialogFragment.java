package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MapTypeSelectDialogFragment extends DialogFragment {

    public MapTypeSelectDialogFragment(){
        mSelected = -1;
    }
    public void setContext(Context context) {
        mContext = context;
    }

    Context mContext;
    Integer mSelected;

    public void setMap(MapView map) {
        mMap = map;
    }

    MapView mMap;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        Integer itemsid = R.array.map_styles;
        Integer checkedItem = -1;

        builder.setTitle(R.string.select_map_style)
                .setSingleChoiceItems(itemsid, checkedItem, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mSelected = id;
                    }
                })
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Change Map
                        if(mSelected != -1){
                            mMap.getMap().setMapType(getMapType(mSelected));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MapTypeSelectDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private int getMapType(int id) {
        HashMap<Integer, Integer> mapping = new HashMap<>();

        String[] mapArray = getResources().getStringArray(R.array.map_styles);

        ArrayList<String> mapArrayList = new ArrayList<>(Arrays.asList(mapArray));

        for(String type : mapArray){
            Integer index = mapArrayList.indexOf(type);

            switch(type){
                case "Satellite":
                    mapping.put(index, GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case "Terrain":
                    mapping.put(index, GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case "Hybrid":
                    mapping.put(index, GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case "Normal":
                    mapping.put(index, GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case "None":
                    mapping.put(index, GoogleMap.MAP_TYPE_NONE);
                    break;
            }
        }

        return mapping.get(id);

    }


}
