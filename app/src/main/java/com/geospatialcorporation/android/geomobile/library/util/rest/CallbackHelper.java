package com.geospatialcorporation.android.geomobile.library.util.rest;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by andre on 4/7/2015.
 */
public class CallbackHelper {

    public static Callback runInsideShell(final CallbackAction action, final CallbackAction failure) {
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                failure.run(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try{
                    if(response.isSuccessful()) {
                        action.run(response);
                    }
                } catch (Exception e){

                }
            }
        };
    }

    public static CallbackAction StandardFailure(final Context context){
        return new CallbackAction(){
            @Override
            public void run(){
                Handler mainHandler = new Handler(context.getMainLooper());

                Runnable runnable = new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(context, "fail", Toast.LENGTH_LONG).show();
                    }
                };

                mainHandler.post(runnable);
            }
        };
    }
}
