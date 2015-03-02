package com.geospatialcorporation.android.geomobile;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Jon Shaffer on 2/26/2015.
 */
public class GeoApiRequest extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;

        try {
            switch (uri[0]) {
                case "DELETE":
                    response = httpclient.execute(new HttpDelete(uri[1]));
                    break;
                case "POST":
                    response = httpclient.execute(new HttpPost(uri[1]));
                    break;
                case "PUT":
                    response = httpclient.execute(new HttpPut(uri[1]));
                    break;
                case "GET":
                    response = httpclient.execute(new HttpGet(uri[1]));
                    break;
                default:
                    response = httpclient.execute(new HttpGet(uri[1]));
                    // TODO: throw exception method not set
                    break;
            }

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }

}
