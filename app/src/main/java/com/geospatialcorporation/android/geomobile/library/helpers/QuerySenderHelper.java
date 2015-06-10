package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.models.Query.point.CenterPoint;
import com.geospatialcorporation.android.geomobile.models.Query.point.Max;
import com.geospatialcorporation.android.geomobile.models.Query.point.Min;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by andre on 6/4/2015.
 */
public class QuerySenderHelper {

    public void sendBoxQuery(Max max, Min min) {

    }

    public void sendPointQuery(LatLng ll) {
        CenterPoint cp = new CenterPoint(ll);


    }
}
