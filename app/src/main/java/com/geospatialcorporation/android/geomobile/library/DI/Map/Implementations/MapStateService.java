package com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.BookmarkMapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Interfaces.IGeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.BookmarkPosition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


public class MapStateService implements IMapStateService {

    IGeoSharedPrefs mSharedPrefs;

    public MapStateService(IGeoSharedPrefs sharedPrefs){
        mSharedPrefs = sharedPrefs;

    }

    //region Constants
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ZOOM = "zoom";
    private static final String BEARING = "bearing";
    private static final String TILT = "tilt";
    private static final String MAPTYPE = "maptype";
    //endregion

    @Override
    public void saveMapState(MapStateSaveRequest request) {
        GoogleMap map = request.getMap();

        if(map != null) {
            CameraPosition position = map.getCameraPosition();

            mSharedPrefs.add(makeKey(LATITUDE), (float) position.target.latitude);
            mSharedPrefs.add(makeKey(LONGITUDE), (float) position.target.longitude);
            mSharedPrefs.add(makeKey(ZOOM), position.zoom);
            mSharedPrefs.add(makeKey(TILT), position.tilt);
            mSharedPrefs.add(makeKey(BEARING), position.bearing);
            mSharedPrefs.add(makeKey(MAPTYPE), map.getMapType());

            mSharedPrefs.apply();
        }
    }

    @Override
    public CameraPosition getSavedCameraPosition() {
        double latitude = mSharedPrefs.getFloat(makeKey(LATITUDE), 0);

        if(latitude == 0){
            return null;
        }

        double longitude = mSharedPrefs.getFloat(makeKey(LONGITUDE), 0);
        LatLng target = new LatLng(latitude, longitude);

        float zoom = mSharedPrefs.getFloat(makeKey(ZOOM), 0);
        float bearing = mSharedPrefs.getFloat(makeKey(BEARING), 0);
        float tilt = mSharedPrefs.getFloat(makeKey(TILT), 0);

        return new CameraPosition(target, zoom, tilt, bearing);
    }

    @Override
    public Integer getSavedMapType() {
        float maptype = mSharedPrefs.getFloat(makeKey(MAPTYPE), 25); //0 is GoogleMap.MAP_TYPE_NONE

        if(maptype == 25){
            return GoogleMap.MAP_TYPE_NORMAL;
        }

        return (int)maptype;
    }

    @Override
    public void saveBookmarkState(BookmarkMapStateSaveRequest request) {
        //IFullDataRepository<Bookmark> BookmarkRepo = new BookmarkDataSource(application.getAppContext()); //not cache repo

        CameraPosition position = request.getMap().getCameraPosition();

        BookmarkPosition bp = new BookmarkPosition();

        bp.setLat((float) position.target.latitude);
        bp.setLng((float) position.target.longitude);
        bp.setZoom(position.zoom);
        bp.setTilt(position.tilt);
        bp.setBearing(position.bearing);

        Bookmark bookmark = new Bookmark(request.getName(), bp);

        //BookmarkRepo.Create(bookmark);
    }

    //region Helpers
    protected String makeKey(String key){
        return key + application.getGeoSubscription().getName();
    }
    //endregion
}
