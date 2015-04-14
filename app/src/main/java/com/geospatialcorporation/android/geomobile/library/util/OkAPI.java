package com.geospatialcorporation.android.geomobile.library.util;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.Media;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.List;

public class OkAPI {
    public String url = "";
    public Callback callback;
    public Object model;
    public List<String> headers = new ArrayList<String>();

    private final OkHttpClient client;
    private final Gson gson;
    public String authToken = "";

    public OkAPI() {
        client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());

        gson = new Gson();
        headers.add("Authorization: WebToken " + application.getAuthToken());
    }

    public OkAPI setCallback(Callback call) {
        callback = call;
        return this;
    }

    public OkAPI setModel(Object m) {
        model = m;
        return this;
    }

    public OkAPI addHeader(String header) {
        if (header.startsWith("Authorization:")) {
            return this;
        }
        headers.add(header);
        return this;
    }

    public void AsyncGet() throws Exception {
        Request.Builder builder = new Request.Builder()
                .url(url);

        for (String header : headers) {
            String[] headerParts = header.split(":");

            builder = builder.addHeader(headerParts[0], headerParts[1]);
        }

        Request request = builder.build();

        client.newCall(request).enqueue(callback);

        /*
        new Callback() {
            @Override public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        }
         */
    }

    public void AsyncPost() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(Media.JSON.value, gson.toJson(model)))
                .build();

        client.newCall(request).enqueue(callback);

        /*
        new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        }
         */
    }

    public void AsyncPut() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(Media.JSON.value, gson.toJson(model)))
                .build();

        client.newCall(request).enqueue(callback);

        /*
        new Callback() {
            @Override public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        }
         */
    }

    public void AsyncDelete() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .delete(RequestBody.create(Media.JSON.value, gson.toJson(model)))
                .build();

        client.newCall(request).enqueue(callback);

        /*
        new Callback() {
            @Override public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        }
         */
    }
}