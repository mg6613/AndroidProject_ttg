package com.android.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.address_book.Group;
import com.android.address_book.People;
import com.android.address_book.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GroupNetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "GroupNetworkTask";
    Context context = null;
    String mAddr = null;
    String where = null;
    ArrayList<Group> group;


    public GroupNetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.where = where;
        this.group = new ArrayList<Group>();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Object o) {

        super.onPostExecute(o);
    }

    @Override
    protected Object doInBackground(Integer... integers) {

        Log.v(TAG, "doInBackground()");
        Log.v(TAG, mAddr);
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String result = null;

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String strline = bufferedReader.readLine();
                    if (strline == null) break;
                    stringBuffer.append(strline + "\n");
                }

                if (where.equals("select")) {
                    parserSelect(stringBuffer.toString());

                } else {
                    result = parserAction(stringBuffer.toString());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        if (where.equals("select")) {
            return group;

        }else{
            return result;

        }

    }


    // select action
    private void parserSelect(String s) {
        Log.v(TAG, "Parser()");

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("relation_info"));
            group.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String groupNo = jsonObject1.getString("relationno");
                String groupName = jsonObject1.getString("relationname");


                Group groups = new Group(groupNo, groupName);
                group.add(groups);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // insert/update action
    private String parserAction(String s) {
        Log.v(TAG, "parserAction()");
        String returnResult = null;

        try {
            JSONObject jsonObject = new JSONObject(s);
            returnResult = jsonObject.getString("result");
            Log.v(TAG, returnResult);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }


}
