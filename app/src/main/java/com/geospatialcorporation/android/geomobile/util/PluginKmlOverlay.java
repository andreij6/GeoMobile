package com.geospatialcorporation.android.geomobile.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class PluginKmlOverlay extends MapPlugin {

    @SuppressLint("UseSparseArrays")
    public void initialize() {
        //
    }

    /**
     * Create kml overlay
     *
     * @param args
     * @throws org.json.JSONException
     */
    @SuppressWarnings("unused")
    private void createKmlOverlay(JSONArray args) throws JSONException {
        return;
        /*JSONObject opts = args.getJSONObject(1);
        Bundle params = PluginUtil.Json2Bundle(opts);

        AsyncKmlParser kmlParser = new AsyncKmlParser(this.cordova.getActivity(), this.mapCtrl, params);
        kmlParser.execute(opts.getString("url"));*/
    }

}
