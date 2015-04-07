package com.geospatialcorporation.android.geomobile.library.util;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.TileOverlay;

public class MapPlugin {
    protected HashMap<String, Object> objects;
    private final String TAG = "GoogleMapsPlugin";
    protected Dialogs dialog;

    public GoogleMap map = null;
    public float density = Resources.getSystem().getDisplayMetrics().density;

    @SuppressLint("UseSparseArrays")
    public void initialize() {
        this.objects = new HashMap<String, Object>();
    }

    public boolean execute(String action, JSONArray args) throws JSONException {
        String[] params = args.getString(0).split("\\.");
        try {
            Method method = this.getClass().getDeclaredMethod(params[1], JSONArray.class);
            method.setAccessible(true);
            method.invoke(this, args);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            dialog.error(e.getMessage());
            return false;
        }
    }

    protected Circle getCircle(String id) {
        return (Circle) this.objects.get(id);
    }


    protected GroundOverlay getGroundOverlay(String id) {
        return (GroundOverlay) this.objects.get(id);
    }

    protected Marker getMarker(String id) {
        return (Marker) this.objects.get(id);
    }

    protected Polyline getPolyline(String id) {
        return (Polyline) this.objects.get(id);
    }

    protected Polygon getPolygon(String id) {
        return (Polygon) this.objects.get(id);
    }

    protected TileOverlay getTileOverlay(String id) {
        return (TileOverlay) this.objects.get(id);
    }

    protected void setInt(String methodName, String id, int value) throws JSONException {
        this.setValue(methodName, int.class, id, value);
    }

    protected void setFloat(String methodName, String id, float value) throws JSONException {
        this.setValue(methodName, float.class, id, value);
    }

    protected void setDouble(String methodName, String id, float value) throws JSONException {
        this.setValue(methodName, double.class, id, value);
    }

    protected void setString(String methodName, String id, String value) throws JSONException {
        this.setValue(methodName, String.class, id, value);
    }

    protected void setBoolean(String methodName, String id, Boolean value) throws JSONException {
        this.setValue(methodName, boolean.class, id, value);
    }

    private void setValue(String methodName, Class methodClass, String id, Object value) throws JSONException {
        Object object = this.objects.get(id);
        try {
            Method method = object.getClass().getDeclaredMethod(methodName, methodClass);
            method.invoke(object, value);
        } catch (Exception e) {
            e.printStackTrace();
            dialog.error(e.getMessage());
        }
        this.sendNoResult();
    }

    public void clear() {
        this.objects.clear();
    }

    protected void sendNoResult() {
        dialog.message("No result.");
    }
}
