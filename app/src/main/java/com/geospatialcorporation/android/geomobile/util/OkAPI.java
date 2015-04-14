package com.geospatialcorporation.android.geomobile.util;

import android.os.AsyncTask;

import com.geospatialcorporation.android.geomobile.application;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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