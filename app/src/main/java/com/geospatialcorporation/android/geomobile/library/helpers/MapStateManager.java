package com.geospatialcorporation.android.geomobile.library.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Bookmark.BookmarkDataSource;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.IGeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.BookmarkPosition;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapStateManager {

    //region Constants
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ZOOM = "zoom";
    private static final String BEARING = "bearing";
    private static final String TILT = "tilt";
    private static final String MAPTYPE = "maptype";
    private static final String PREFS_NAME = "mapCameraState";
    //endregion

    private SharedPreferences mapStatePrefs;
    private Client mClient;

    public MapStateManager(Context context){
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mClient = application.getGeoClient();
    }

    public void saveMapState(GoogleMap map){
        SharedPreferences.Editor editor = mapStatePrefs.edit();

        CameraPosition position = map.getCameraPosition();

        editor.putFloat(makeKey(LATITUDE), (float) position.target.latitude);
        editor.putFloat(makeKey(LONGITUDE), (float)position.target.longitude);
        editor.putFloat(makeKey(ZOOM), position.zoom);
        editor.putFloat(makeKey(TILT), position.tilt);
        editor.putFloat(makeKey(BEARING), position.bearing);
        editor.putInt(makeKey(MAPTYPE), map.getMapType());

        editor.apply();
    }

    private String makeKey(String key){
        return key + mClient.getName();
    }

    public CameraPosition getSavedCameraPosition(){
        double latitude = mapStatePrefs.getFloat(makeKey(LATITUDE), 0);

        if(latitude == 0){
            return null;
        }

        double longitude = mapStatePrefs.getFloat(makeKey(LONGITUDE), 0);
        LatLng target = new LatLng(latitude, longitude);

        float zoom = mapStatePrefs.getFloat(makeKey(ZOOM), 0);
        float bearing = mapStatePrefs.getFloat(makeKey(BEARING), 0);
        float tilt = mapStatePrefs.getFloat(makeKey(TILT), 0);

        return new CameraPosition(target, zoom, tilt, bearing);

    }

    public Integer getSavedMapType(){
        Integer maptype = mapStatePrefs.getInt(makeKey(MAPTYPE), 25); //0 is GoogleMap.MAP_TYPE_NONE

        if(maptype == 25){
            return GoogleMap.MAP_TYPE_NORMAL;
        }

        return maptype;
    }

    public void saveMapStateForBookMark(GoogleMap map, String name) {
        IFullDataRepository<Bookmark> BookmarkRepo = new BookmarkDataSource(application.getAppContext()); //not cache repo

        CameraPosition position = map.getCameraPosition();

        BookmarkPosition bp = new BookmarkPosition();

        bp.setLat((float) position.target.latitude);
        bp.setLng((float) position.target.longitude);
        bp.setZoom(position.zoom);
        bp.setTilt(position.tilt);
        bp.setBearing(position.bearing);

        Bookmark bookmark = new Bookmark(name, bp);

        BookmarkRepo.Create(bookmark);
    }
}
