
package petym.android.com.petym.DataDownload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.R;
import petym.android.com.petym.VO.Member;
import petym.android.com.petym.VO.MemberPetResturant;
import petym.android.com.petym.VO.Pet;
import petym.android.com.petym.VO.Restaurant;

/**
 * Created by k3nt on 2017/8/2.
 Pet pet;
 int id = dateItemVO.getDateItemNo();
 int petId = dateItemVO.getpetNo();
 if (networkConnected()) {
 try {
 pet =new GetPet(this).execute(Common.URL+ "Pet",restId).get();
 } catch (Exception e) {
 e.printStackTrace();
 }
 } else {
 Toast.makeText(this, "cool", Toast.LENGTH_SHORT);
 }
 Or
 */


public class GetPetRestuarantNoItemUpload extends AsyncTask<Object, Integer,ArrayList<Integer>> {
    private final static String TAG = "SimpleRestuatant";
    private static String ACTION = "memberPetResturant";
    private ProgressDialog progressDialog;
    private Activity activity;

    public GetPetRestuarantNoItemUpload(Activity a, String ACTION) {
        this.activity = a;
        this.ACTION = ACTION;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    protected ArrayList<Integer> doInBackground(Object... params) {
        String jsonIn;
        String url = params[0].toString();
        JsonObject jsonObject = new JsonObject();
        String restuarantSelected = params[1].toString();
        String petSelected = params[2].toString();


        jsonObject.addProperty("restuarantSelected", restuarantSelected);
        jsonObject.addProperty("petSelected", petSelected);

        try {
            jsonIn = getRemoteImage(url, jsonObject.toString());
        } catch (IOException e) {

            return null;
        }
        Log.d("jsonIn: ", "jsonInQQ: " + jsonIn);
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type typelist = new TypeToken<ArrayList<Integer>>(){}.getType();
        return gson.fromJson(jsonIn, typelist);

    }





    @Override
    protected void onPostExecute(ArrayList<Integer> petRestNoArrayList) {

        super.onPostExecute(petRestNoArrayList);
        progressDialog.cancel();
    }

    private String getRemoteImage(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine())!=null){
                jsonIn.append(line);
            }
            Log.d(TAG, "jsonInqq: " + jsonIn);
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        return jsonIn.toString();
    }
}