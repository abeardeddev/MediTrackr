package com.meditrackrv2.meditrackr.services;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Andrew on 17/06/2014.
 */
public class UPCSearchService extends AbstractService {

    private String upc_ID;
    private JSONObject upc;

    public UPCSearchService(String upc_ID) {
        this.upc_ID = upc_ID;
    }

    public JSONObject getResult(){
        return upc;
    }

    public void run(){

        String api_key = "08b1d0bfcc9d920c4457c88db8e90ba0";
        String URL = "http://api.upcdatabase.org/json/" + api_key + "/" + upc_ID;

        Log.i("Url", URL);

        boolean error = false;
        HttpClient httpclient = null;

        try {
            httpclient = new DefaultHttpClient();
            HttpResponse data = httpclient.execute(new HttpGet(URL));
            HttpEntity entity = data.getEntity();
            String result = EntityUtils.toString(entity, "UTF8");
            upc = new JSONObject(result);
        } catch (Exception e) {
            upc = null;
            error = true;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        super.serviceComplete(error);

    }
}
