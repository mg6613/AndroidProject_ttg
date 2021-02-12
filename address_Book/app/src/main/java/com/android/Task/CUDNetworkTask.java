package com.android.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.android.address_book.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CUDNetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "NetworkTask";
    Context context = null;
    String mAddr = null;
    String where = null;
    ProgressDialog progressDialog = null;
    int favoriteCheck = 0;
    int emergencyCheck = 0;

    public CUDNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
    }

    public CUDNetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.where = where;
        Log.v(TAG, "Start : " + mAddr);;
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Create/Update/Delete");
//        progressDialog.setMessage("Working ....");
//        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "onPostExecute()");
        super.onPostExecute(o);
//        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG,"onCancelled()");
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        FileOutputStream fileOutputStream =null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String result = null;


        try{
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);


            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while(true){
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }

                if(where.equals("Register")){
                    result = parserRegister(stringBuffer.toString());
                }
                if(where.equals("modifyPeople")){
                    parserModifyPeople(stringBuffer.toString());
                }
                if(where.equals("favoriteCount")){
                    parserfavoriteCheck(stringBuffer.toString());
                }
                if(where.equals("deletePeople")){
                    parserDeletePeople(stringBuffer.toString());
                }
//                else{
//                    result = parserAction(stringBuffer.toString());
//                }

            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();

            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        if(where.equals("Register")){
           return result;

        }
        if(where.equals("modifyPeople")){
            return null;}

        if(where.equals("deletePeople")){
            return null;}

        if(where.equals("favoriteCount")) {
            return favoriteCheck;

        } else if(where.equals("emergencyCount")){
            return emergencyCheck;

        } else{
            return result;
        }

    }


    // update action
    private String parserRegister(String s){
        Log.v(TAG,"parserModifyPeople()");
        String returnResult = null;

        try{
            JSONObject jsonObject = new JSONObject(s);
            returnResult = jsonObject.getString("result");
            Log.v(TAG, returnResult);

        } catch (Exception e){
            e.printStackTrace();
        }

        return returnResult;
    }

    // delete action
    private void parserDeletePeople(String s){
//        Log.v(TAG,"parserDeletePeople()");
//        String returnResult = null;

        try{
//            JSONObject jsonObject = new JSONObject(s);
//            returnResult = jsonObject.getString("result");
//            Log.v(TAG, returnResult);

        } catch (Exception e){
            e.printStackTrace();
        }

//        return returnResult;
    }

    private void parserfavoriteCheck(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parserModifyPeople(String s){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }





} // end
