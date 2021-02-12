package com.android.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.address_book.People;
import com.android.address_book_Activity.FirstFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PeopleNetworkTask extends AsyncTask<Integer, String, Object> {
    final static String TAG = "PeopleNetworkTask";
    Context context = null;
    FirstFragment firstFragment = null;
    String mAddr = null;
    String where = null;
    ProgressDialog progressDialog = null;
    ArrayList<People> members;

    public PeopleNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.members = new ArrayList<People>();
        Log.v(TAG, "Start : "+ mAddr);
    }

//    public PeopleNetworkTask(FirstFragment firstFragment, String mAddr) {
//        this.firstFragment = firstFragment;
//        this.mAddr = mAddr;
//        this.members = new ArrayList<People>();
//        Log.v(TAG, "Start : "+ mAddr);
//    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Data Fetch");
//        progressDialog.setMessage("Get...");
//        progressDialog.show();
    }



    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");

        StringBuffer stringBuffer = new StringBuffer();

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        BufferedReader bufferedReader = null;
        Log.v(TAG, "before try");
        try{
            Log.v(TAG, "after try");
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            Log.v(TAG, "Accept : "+httpURLConnection.getResponseCode());
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true){
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");

                }
                Log.v(TAG, "StringBuffer : "+stringBuffer.toString());
                Log.v("here","parser11");

                    peopleParser(stringBuffer.toString());


            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

            return members;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "doProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "doPostExecute()");
        super.onPostExecute(o);
//        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled()");
        super.onCancelled();
    }

    private void peopleParser(String s){
        Log.v(TAG, "parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("people_info"));
            Log.v(TAG, "parser() in");
            members.clear();
            for(int i = 0 ; i<jsonArray.length() ; i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String no = jsonObject1.getString("no");
                String name = jsonObject1.getString("name");

                ArrayList<String> phones = new ArrayList<String>();
                JSONArray jsonArray1 = jsonObject1.getJSONArray("tel");
                for (int j=0; j<jsonArray1.length(); j++){
                    String phone = jsonArray1.getString(j);
                    phones.add(phone);
                }

                String email = jsonObject1.getString("email");
                String relation = jsonObject1.getString("relation");
                String memo = jsonObject1.getString("memo");
                String image = jsonObject1.getString("image");
                String favorite = jsonObject1.getString("favorite");
                String emergency = jsonObject1.getString("emergency");
                String useremail = jsonObject1.getString("useremail");

                ArrayList<Integer> phonenum = new ArrayList<Integer>();
                JSONArray jsonArray2 = jsonObject1.getJSONArray("phoneno");
                for (int k=0; k<jsonArray2.length(); k++){
                    int phone = jsonArray2.getInt(k);
                    phonenum.add(phone);
                }

//                ArrayList<String> phonenoes = new ArrayList<String>();
//                String phoneno = jsonObject1.getString("phoneno");
//

//                for (int k=0; k<jsonArray2.length(); k++){
//                    int phoneno = Integer.parseInt(jsonArray2.getString(k));
//                    Log.v(TAG, Integer.toString(phoneno));
//                    phonenoes.add("1");
//                }
//


                People people = new People(no, name, phones, email, relation, memo, image, favorite, emergency, useremail, phonenum);
//                People people = new People(no, name, phones, email, relation, memo, image, favorite, emergency, useremail, phoneno);
                members.add(people);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
