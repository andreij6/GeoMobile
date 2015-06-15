package com.geospatialcorporation.android.geomobile.library.helpers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.QueryService;
import com.geospatialcorporation.android.geomobile.models.Query.Options;
import com.geospatialcorporation.android.geomobile.models.Query.box.BoxParameters;
import com.geospatialcorporation.android.geomobile.models.Query.box.BoxQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.point.CenterPoint;
import com.geospatialcorporation.android.geomobile.models.Query.point.Max;
import com.geospatialcorporation.android.geomobile.models.Query.point.Min;
import com.geospatialcorporation.android.geomobile.models.Query.point.PointParameters;
import com.geospatialcorporation.android.geomobile.models.Query.point.PointQueryRequest;
import com.google.android.gms.maps.model.LatLng;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//  ---
//  Need to know which layers are showing before can sending
//
// ---
public class QuerySenderHelper {
    private static final String TAG = QuerySenderHelper.class.getSimpleName();

    QueryService mService;

    public QuerySenderHelper(){
        mService = application.getRestAdapter().create(QueryService.class);
    }

    public void sendBoxQuery(Max max, Min min) {
        /*
        BoxQueryRequest bqr = new BoxQueryRequest();

        bqr.setOptions(new Options());
        bqr.setBoxParameters(new BoxParameters());

        mService.boxQuery(bqr, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        */
    }

    public void sendPointQuery(LatLng ll) {
        /*
        CenterPoint cp = new CenterPoint(ll);

        PointQueryRequest pqr = new PointQueryRequest();

        pqr.setOptions(new Options());
        pqr.setParameters(new PointParameters());

        mService.pointQuery(pqr, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        **/
    }
}
