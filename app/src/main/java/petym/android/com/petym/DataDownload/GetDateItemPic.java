package petym.android.com.petym.DataDownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import petym.android.com.petym.VO.DateItemVO;
import petym.android.com.petym.R;

/**
 * Created by k3nt on 2017/8/2.
 */


public class GetDateItemPic extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "DateItemImageTask";
    private final static String ACTION = "getImage";
    private final WeakReference<ImageView> imageViewWeakReference;
    private final DateItemVO dateItemVO;


    public GetDateItemPic(ImageView imageView, DateItemVO dateItemVO) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
        this.dateItemVO = dateItemVO;
    }
    @Override
    protected Bitmap doInBackground(Object... params) {
        String url = params[0].toString();
        int id = Integer.parseInt(params[1].toString());
        int imageSize = Integer.parseInt(params[2].toString());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateItem", ACTION);
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("imageSize", imageSize);

        Bitmap bitmap;
        try {
            bitmap = getRemoteImage(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        ImageView imageView = imageViewWeakReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.default_image);
            }
        }
        super.onPostExecute(bitmap);
    }

    private Bitmap getRemoteImage(String url, String jsonOut) throws IOException {
        Bitmap bitmap = null;
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
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        return bitmap;
    }
}