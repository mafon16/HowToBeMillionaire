package com.test.bemillionaire.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Connection {
    private static final String TOKEN_TYPE="bearer";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    private static final int HTTP_ERROR_COD = 401;
    private static final String TAG = "WES/Connection";
    private static final String CHARSET = "UTF-8";
    private static final String HEADER="application/json";
    private HttpURLConnection httpConnection;
    private DataOutputStream dataOutputStream;
    private StringBuilder result;
    private URL urlObj;
    private StringBuilder sbParams;
    //RequestParams
    private String accessToken;
    private String url;
    private String typeRequest;
    private HashMap<String,String> paramMap;
    private HttpResponseListener errorListener;

    Context context;
    int responseCode;

    public Connection(Context context){
        this.context=context;
    }


    public  JSONArray makeRequest( String typeRequest, String uri, HashMap<String, String> paramMap){

        ArrayList<RequestParams> httpParamsList = new ArrayList<>();
        RequestParams paramsObj = new RequestParams();
        paramsObj.setTypeRequest(typeRequest);
        paramsObj.setTokentype(TOKEN_TYPE);
        paramsObj.setUrl(uri);
        paramsObj.setParams(paramMap);
        httpParamsList.add(paramsObj);

        JSONArray jsonArray=null;

            try {
                jsonArray= new HttpAsyncTask().execute(httpParamsList).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return jsonArray;

    }


    public class HttpAsyncTask extends AsyncTask<ArrayList<RequestParams>, String, JSONArray> {
        ProgressDialog pDialog;
        @Override
        public void onPreExecute() {
            Log.d(TAG, "onPreExecute");
            pDialog = new ProgressDialog(context);
            pDialog .setTitle("Title");
            pDialog .setMessage("Message");
            pDialog.show();
        }

        @Override
        public JSONArray doInBackground(ArrayList<RequestParams>... params) {
            Log.d(TAG, "doInBackground");

            ArrayList<RequestParams> listParams = params[0];
            JSONArray jsonArray=setHttpConnection(listParams);
            return jsonArray;
        }


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            Log.d(TAG, "onPostExecute");
            pDialog.dismiss();
        }
    }


    public JSONArray setHttpConnection(ArrayList<RequestParams> params) {
        accessToken = params.get(0).getAccessToken();
        typeRequest = params.get(0).getTypeRequest();
        url = params.get(0).getUrl();


        try {
            urlObj = new URL(url);
            httpConnection = (HttpURLConnection) urlObj.openConnection();
            httpConnection.setRequestMethod(typeRequest);
            httpConnection.setRequestProperty("Accept-Charset", CHARSET);
            httpConnection.setRequestProperty("Accept", HEADER);
            httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            httpConnection.setReadTimeout(10000);
            httpConnection.setConnectTimeout(15000);
            httpConnection.connect();

            if (typeRequest == POST) {
                paramMap = params.get(0).getParams();
                sbParams = new StringBuilder();
                int i = 0;
                for (String key : paramMap.keySet()) {
                    try {
                        if (i != 0) {
                            sbParams.append("&");
                        }
                        sbParams.append(key).append("=").append(URLEncoder.encode(paramMap.get(key), CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                dataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
                dataOutputStream.writeBytes(sbParams.toString());
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            Log.d(TAG, "http response status=" + httpConnection.getResponseCode());
            if (httpConnection.getResponseCode() == HTTP_ERROR_COD) {
                httpConnection.disconnect();
            }


        } catch (MalformedURLException e) {
            Log.d(TAG, "try again)), MalformedURLException ..");
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.d(TAG, "try again)), ProtocolException ..");
            e.printStackTrace();
        } catch (ConnectException e) {
            Log.d(TAG, "try again)), ConnectException....");
            //    errorListener.handleError();
            e.printStackTrace();
        } catch (RuntimeException e) {
            Log.d(TAG, "try again)), RuntimeException....");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "try again)), IOException ..");
            // errorListener.handleError();
        }

        JSONArray jsonArray=getResponse(httpConnection);
         return jsonArray;
    }


    public JSONArray getResponse(HttpURLConnection httpConnection) {

        try {
            InputStream in = new BufferedInputStream(httpConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                Log.d(TAG, "result: " + result.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonResponse=null;
        try {
           jsonResponse = new JSONArray(result.toString());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }
        httpConnection.disconnect();
         return jsonResponse;
    }



}
