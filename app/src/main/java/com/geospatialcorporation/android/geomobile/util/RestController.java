package com.geospatialcorporation.android.geomobile.util;

import android.os.AsyncTask;

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

public class RestController {
    private final OkHttpClient client;
    private final Gson gson;
    public String authToken = "";

    public RestController(String AuthToken) {
        client = new OkHttpClient();
        gson = new Gson();
        authToken = AuthToken;
    }

    public void AsyncGet(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
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
        });
    }

    public void AsyncPost(String url, Object model) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(Media.JSON.value, gson.toJson(model)))
                .build();

        client.newCall(request).enqueue(new Callback() {
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
        });
    }

    public void AsyncPut(String url, Object model) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(Media.JSON.value, gson.toJson(model)))
                .build();

        client.newCall(request).enqueue(new Callback() {
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
        });
    }

    public void AsyncDelete(String url, Object model) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .delete(RequestBody.create(Media.JSON.value, gson.toJson(model)))
                .build();

        client.newCall(request).enqueue(new Callback() {
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
        });
    }
}