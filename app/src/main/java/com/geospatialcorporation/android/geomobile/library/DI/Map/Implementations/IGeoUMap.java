package com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations;

import android.app.Activity;
import android.content.Context;

import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;

public interface IGeoUMap {
    interface  Configuration {
        GoogleMap getMap();

        void setMap(GoogleMap map);

        UiSettings getUiSettings();

        void setUiSettings(UiSettings uiSettings);

        ILayerManager getLayerManager();

        void setLayerManager(ILayerManager layerManager);

        Context getContext();

        void setContext(Context context);

        GoogleApiClient getLocationClient();

        void setLocationClient(GoogleApiClient locationClient);

        Activity getActivity();

        void setActivity(Activity activity);
    }

    interface LifeCycle {
        void onPause();

        void onDestroy();

        void onLowMemory();

        void onResume();

        void onStop();
    }
}
