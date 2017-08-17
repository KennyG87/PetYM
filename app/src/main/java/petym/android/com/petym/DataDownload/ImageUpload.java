package petym.android.com.petym.DataDownload;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// for insert, ic_update, ic_delete a spot
class ImageUpload extends AsyncTask<Object, Integer, Void> {
    private final static String TAG = "ImageUpload";

    @Override
    protected Void doInBackground(Object... params) {
        List<byte[]> imageUpload = new ArrayList<byte[]>();
        byte[] image = (byte[]) params[1];
        String url = params[0].toString();
        imageUpload.add(new byte[0]);
        imageUpload.add(image);
        Gson gson = new Gson();
        String jsonIn = gson.toJson(imageUpload);
        try {
            getRemoteData(url, jsonIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();
        return ;
    }
}