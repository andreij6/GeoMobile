package com.geospatialcorporation.android.geomobile.library.util;

import android.content.Context;
import android.net.*;
import android.os.Build;

public class ConnectionDetector {

    Context mContext;

    public ConnectionDetector(Context context){
        mContext = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);

        if(connectivity != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivity.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivity.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            }else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
