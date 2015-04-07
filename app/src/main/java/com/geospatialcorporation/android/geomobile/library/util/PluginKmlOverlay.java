package com.geospatialcorporation.android.geomobile.library.util;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;

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
