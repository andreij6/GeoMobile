package com.geospatialcorporation.android.geomobile.library.util;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GeoApi {
    public String authToken = "";

    public GeoApi(String AuthToken) {
        authToken = AuthToken;
    }

    public class GeoApiRequest extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            JSONObject response = new JSONObject();

            switch (uri[0]) {
                case "DELETE":
                    response = httpDelete(httpclient, uri[1]);
                    break;
                case "POST":
                    response = httpPost(httpclient, uri[1]);
                    break;
                case "PUT":
                    response = httpPut(httpclient, uri[1]);
                    break;
                case "GET":
                    response = httpGet(httpclient, uri[1]);
                    break;
                default:
                    break;
            }

            return response;
        }

        private JSONObject httpDelete(HttpClient client, String uri) {
            try {
                HttpDelete request = new HttpDelete(uri);
                request.setHeader("Authentication", "WebToken " + authToken);

                HttpResponse response = client.execute(request);

                return new JSONObject(response.getEntity().getContent().toString());
            } catch (JSONException e) {
                // TODO Handle problems..
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            return new JSONObject();
        }

        private JSONObject httpPut(HttpClient client, String uri) {
            try {
                HttpPut request = new HttpPut(uri);
                request.setHeader("Authentication", "WebToken " + authToken);

                HttpResponse response = client.execute(request);

                return new JSONObject(response.getEntity().getContent().toString());
            } catch (JSONException e) {
                // TODO Handle problems..
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            return new JSONObject();
        }

        private JSONObject httpPost(HttpClient client, String uri) {
            try {
                HttpPost request = new HttpPost(uri);
                request.setHeader("Authentication", "WebToken " + authToken);

                HttpResponse response = client.execute(request);

                return new JSONObject(response.getEntity().getContent().toString());
            } catch (JSONException e) {
                // TODO Handle problems..
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            return new JSONObject();
        }

        private JSONObject httpGet(HttpClient client, String uri) {
            try {
                HttpGet request = new HttpGet(uri);
                request.setHeader("Authentication", "WebToken " + authToken);

                HttpResponse response = client.execute(request);

                return new JSONObject(response.getEntity().getContent().toString());
            } catch (JSONException e) {
                // TODO Handle problems..
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }

            return new JSONObject();
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            //Do anything with response..
        }

    }
}