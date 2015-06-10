package com.geospatialcorporation.android.geomobile.library.map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.IAppDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.library.rest.SublayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit.RetrofitError;

public class MapActions {
    protected final static String TAG = MapActions.class.getSimpleName();
    private GoogleMap mMap;
    private LayerService layerService;
    private SublayerService sublayerService;
    private IAppDataRepository<Layer> LayerRepo;

    public MapActions() {
        GoogleMapFragment mMapFragment = application.getMapFragment();
        layerService = application.getRestAdapter().create(LayerService.class);
        sublayerService = application.getRestAdapter().create(SublayerService.class);
        mMap = mMapFragment.mMap;
        LayerRepo = new LayersAppSource();
    }

    public void showLayer(Layer layer) {
        if (layer == null) {
            showNullLayer(0);
        } else {
            showLayerData(layer);
        }
    }

    public void showLayer(int layerId) {
        Layer layer = LayerRepo.getById(layerId);

        if (layer == null) {
            showNullLayer(layerId);
        } else {
            showLayerData(layer);
        }
    }

    private void showNullLayer(Integer layerId) {
        Log.d(TAG, "Layer not found, updating application layers.");
        new getLayersTask().execute(layerId);
    }

    private void showLayerData(Layer layer) {
        int geometryTypeCode = layer.getGeometryTypeCodeId();
        if (layer == null) {
            Toast.makeText(application.getAppContext(), "Layer showing failed, layer not found.", Toast.LENGTH_LONG).show();
        }

        try {
            switch (geometryTypeCode) {
                case GeometryTypeCodes.Point:
                    showPoint(layer);
                    break;
                case GeometryTypeCodes.MultiPoint:
                    showMultiPoint(layer);
                    break;
                case GeometryTypeCodes.Line:
                    showLine(layer);
                    break;
                case GeometryTypeCodes.MultiLine:
                    showMultiLine(layer);
                    break;
//                case GeometryTypeCodes.Polygon:
//                    showPolygon(layer);
//                    break;
//                case GeometryTypeCodes.MultiPolygon:
//                    showMultiPolygon(layer);
//                    break;
//                case GeometryTypeCodes.Raster:
//                    showRaster(layer);
//                    break;
                default:
                    Toast.makeText(application.getAppContext(), "Layer type " + layer.getGeometryTypeCodeId() + " not yet implemented", Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(application.getAppContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showPoint(Layer layer) {
        MarkerOptions point = new MarkerOptions().position(layer.getExtent().getMinLatLng());
        point.flat(true);

        layer.setMapObject(mMap.addMarker(point));
        layer.setIsShowing(true);
    }

    private void showMultiPoint(Layer layer) {
        List<Layer> sublayers = getLayerSublayers(layer);

        if (sublayers != null) {
            for (Layer sublayer : sublayers) {
                showLayer(sublayer);
            }

            layer.setIsShowing(true);
        }
    }

    private void showLine(Layer layer) {
        PolylineOptions polyline = new PolylineOptions();

        List<Layer> sublayers = getLayerSublayers(layer);
        Layer.StyleInfo styleInfo = getLayerStyleInfo(layer);

        if (sublayers != null && styleInfo != null) {
            for (Layer sublayer : sublayers) {
                polyline.add(sublayer.getExtent().getMinLatLng(), sublayer.getExtent().getMaxLatLng());
            }

            polyline.width(styleInfo.Width);
            polyline.color(Color.parseColor(styleInfo.BorderColor));

            layer.setMapObject(mMap.addPolyline(polyline));
            layer.setIsShowing(true);
        }
    }

    private void showMultiLine(Layer layer) {
        List<Layer> sublayers = getLayerSublayers(layer);

        if (sublayers != null) {
            for (Layer sublayer : sublayers) {
                showLayer(sublayer);
            }

            layer.setIsShowing(true);
        }
    }

    private void showPolygon(Layer layer) {
        List<Layer> sublayers = getLayerSublayers(layer);

        layer.setMapObject(mMap.addPolygon(new PolygonOptions()));
        layer.setIsShowing(true);
    }

    private void showMultiPolygon(Layer layer) {
        List<Layer> sublayers = getLayerSublayers(layer);

        for (Layer sublayer : sublayers) {
            showLayer(sublayer);
        }

        layer.setIsShowing(true);
    }

    private List<Layer> getLayerSublayers(Layer layer) {
        if (layer.getSublayers() == null) {
            new getSublayersTask().execute(layer);
        }

        return layer.getSublayers();
    }

    private Layer.StyleInfo getLayerStyleInfo(Layer layer) {
        if (layer.getStyleInfo() == null) {
            new getStyleInfoTask().execute(layer);
        }

        return layer.getStyleInfo();
    }

    private class getSublayersTask extends AsyncTask<Layer, Object, List<Layer>> {
        private Layer layer;

        @Override
        protected List<Layer> doInBackground(Layer[] params) {
            List<Layer> sublayers = null;
            try {
                layer = params[0];
                sublayers = sublayerService.getSublayers(layer.getId());
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return sublayers;
        }

        @Override
        protected void onPostExecute(List<Layer> newSublayers) {
            layer.setSublayers(newSublayers);
            showLayer(layer);
        }
    }

    private class getStyleInfoTask extends AsyncTask<Layer, Object, Layer.StyleInfo> {
        private Layer layer;

        @Override
        protected Layer.StyleInfo doInBackground(Layer[] params) {
            Layer.StyleInfo styleInfo = null;
            try {
                layer = params[0];
                styleInfo = layerService.getStyle(layer.getId());
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return styleInfo;
        }

        @Override
        protected void onPostExecute(Layer.StyleInfo styleInfo) {
            layer.setStyleInfo(styleInfo);
        }
    }

    private class getLayersTask extends AsyncTask<Integer, Object, List<Layer>> {
        Integer selectedLayerId;

        @Override
        protected List<Layer> doInBackground(Integer[] params) {
            List<Layer> layers = null;
            try {
                selectedLayerId = params[0];
                layers = layerService.getLayers();
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
                }
            }

            return layers;
        }

        @Override
        protected void onPostExecute(List<Layer> layers) {
            if (layers != null) {
                IAddDataRepository<Layer> LayerRepo = new LayersAppSource();
                LayerRepo.Add(layers);
                showLayer(selectedLayerId);
            } else {
                Log.e(TAG, "Failed to update application layers.");
            }
        }
    }

}