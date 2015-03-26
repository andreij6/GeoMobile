package com.geospatialcorporation.android.geomobile.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;

public class PluginGroundOverlay extends MapPlugin {
  private BitmapDescriptor dummyImg;
  
  @SuppressLint("UseSparseArrays")
  @Override
  public void initialize() {
    super.initialize();
    Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    this.dummyImg = BitmapDescriptorFactory.fromBitmap(bitmap);
  }

  /**
   * Create ground overlay
   * 
   * @param args
   * @throws org.json.JSONException
   */
  @SuppressWarnings("unused")
  private void createGroundOverlay(final JSONArray args) throws JSONException {

    JSONObject opts = args.getJSONObject(1);

    GroundOverlayOptions options = new GroundOverlayOptions();
    
    if (opts.has("anchor")) {
      JSONArray anchor = opts.getJSONArray("anchor");
      options.anchor((float)anchor.getDouble(0), (float)anchor.getDouble(1));
    }
    if (opts.has("bearing")) {
      options.bearing((float)opts.getDouble("bearing"));
    }
    if (opts.has("opacity")) {
      options.transparency(1 - (float)opts.getDouble("opacity"));
    }
    if (opts.has("zIndex")) {
      options.zIndex((float)opts.getDouble("zIndex"));
    }
    if (opts.has("visible")) {
      options.visible(opts.getBoolean("visible"));
    }
    
    if (opts.has("bounds") == true) {
      JSONArray points = opts.getJSONArray("bounds");
      LatLngBounds bounds = PluginUtil.JSONArray2LatLngBounds(points);
      options.positionFromBounds(bounds);
    }

    // Load a dummy image
    options.image(this.dummyImg);
    
    GroundOverlay groundOverlay = this.map.addGroundOverlay(options);
    
    // Load image
    String url = opts.getString("url");
    _setImage(groundOverlay, url);
  }
  private void _setImage(final GroundOverlay groundOverlay, final String url) {
    if (url != null && url.length() > 0) {
      if (url.indexOf("http") == 0) {
        AsyncLoadImage task = new AsyncLoadImage(new AsyncLoadImageInterface() {

          @Override
          public void onPostExecute(Bitmap image) {
            if (image == null) {
              dialog.error("Can not load image from " + url);
              return;
            }
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(image);
            groundOverlay.setImage(bitmapDescriptor);
            _success(groundOverlay);
          }
        
        });
        task.execute(url);
      } else {
        groundOverlay.setImage(BitmapDescriptorFactory.fromAsset(url));
        _success(groundOverlay);
      }
    }
  }
  
  private void _success(GroundOverlay groundOverlay) {

    String id = "groundOverlay_" + groundOverlay.getId();
    this.objects.put(id, groundOverlay);

    JSONObject result = new JSONObject();
    try {
      result.put("hashCode", groundOverlay.hashCode());
      result.put("id", id);
    } catch (Exception e) {}
    dialog.success(result);
  }

  /**
   * Remove this tile layer
   * @param args
   * @throws org.json.JSONException
   */
  protected void remove(JSONArray args) throws JSONException {
    String id = args.getString(1);
    GroundOverlay groundOverlay = (GroundOverlay)this.objects.get(id);
    if (groundOverlay == null) {
      this.sendNoResult();
      return;
    }
    groundOverlay.remove();
    this.sendNoResult();
  }

  /**
   * Set visibility for the object
   * @param args
   * @throws org.json.JSONException
   */
  protected void setVisible(JSONArray args) throws JSONException {
    boolean visible = args.getBoolean(2);
    
    String id = args.getString(1);
    GroundOverlay groundOverlay = (GroundOverlay)this.objects.get(id);
    if (groundOverlay == null) {
      this.sendNoResult();
      return;
    }
    groundOverlay.setVisible(visible);
    this.sendNoResult();
  }
  

  /**
   * Set image of the ground-overlay
   * @param args
   * @throws org.json.JSONException
   */
  @SuppressWarnings("unused")
  private void setImage(final JSONArray args) throws JSONException {
    String id = args.getString(1);
    GroundOverlay groundOverlay = (GroundOverlay)this.objects.get(id);
    String url = args.getString(2);
    
    // Load image
    _setImage(groundOverlay, url);
  }
  

  /**
   * Set bounds
   * @param args
   * @throws org.json.JSONException
   */
  @SuppressWarnings("unused")
  private void setBounds(final JSONArray args) throws JSONException {
    String id = args.getString(1);
    GroundOverlay groundOverlay = (GroundOverlay)this.objects.get(id);
    
    JSONArray points = args.getJSONArray(2);
    LatLngBounds bounds = PluginUtil.JSONArray2LatLngBounds(points);
    groundOverlay.setPositionFromBounds(bounds);

    this.sendNoResult();
  }

  /**
   * Set opacity
   * @param args
   * @throws org.json.JSONException
   */
  @SuppressWarnings("unused")
  private void setOpacity(final JSONArray args) throws JSONException {
    float alpha = (float)args.getDouble(2);
    String id = args.getString(1);
    this.setFloat("setTransparency", id, 1 - alpha);
  }
  /**
   * Set bearing
   * @param args
   * @throws org.json.JSONException
   */
  @SuppressWarnings("unused")
  private void setBearing(final JSONArray args) throws JSONException {
    float bearing = (float)args.getDouble(2);
    String id = args.getString(1);
    this.setFloat("setBearing", id, bearing);
  }
  /**
   * set z-index
   * @param args
   * @throws org.json.JSONException
   */
  @SuppressWarnings("unused")
  private void setZIndex(final JSONArray args) throws JSONException {
    String id = args.getString(1);
    float zIndex = (float) args.getDouble(2);
    this.setFloat("setZIndex", id, zIndex);
  }
}
