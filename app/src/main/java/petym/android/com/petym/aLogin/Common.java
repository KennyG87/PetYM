package petym.android.com.petym.aLogin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.logging.ConsoleHandler;

import petym.android.com.petym.Test.MainActivity_Test;
import petym.android.com.petym.VO.Member;

/**
 * Created by k3nt on 2017/7/21.
 */

public class Common extends AppCompatActivity{
    // Android官方模擬器連結本機web server可以直接使用 http://10.0.2.2
//	public static String URL = "http://192.168.196.202:8080/TextToJson_Web/SearchServlet";
//    public final static String URL = "http://10.0.2.2:8081/TextToJson_Web/SearchServlet";
    public final static String URL = "http://10.0.2.2:8081/PetYM_SQL_Web/";
    private static final String TAG = "Common";
    public final static String PREF_FILE = "preference";
    private final static String FILE_NAME1 = "member.txt";
    private Member member;
    private ObjectInputStream ois = null;



    public static byte[] bitmapToPNG(Bitmap srcBitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // 設定長寬不超過scaleSize
    public static Bitmap downSize(Bitmap srcBitmap, int newSize) {
        if (newSize <= 1) {
            // 如果欲縮小的尺寸過小，就直接定為128
            newSize = 128;
        }
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        String text = "source image size = " + srcWidth + "x" + srcHeight;
        Log.d(TAG, text);
        int longer = Math.max(srcWidth, srcHeight);

        if (longer > newSize) {
            double scale = longer / (double) newSize;
            int dstWidth = (int) (srcWidth / scale);
            int dstHeight = (int) (srcHeight / scale);
            srcBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, false);
            System.gc();
            text = "\nscale = " + scale + "\nscaled image size = " +
                    srcBitmap.getWidth() + "x" + srcBitmap.getHeight();
            Log.d(TAG, text);
        }
        return srcBitmap;
    }

    public  Member getMemberAccount() {

    final  String FILE_NAME1 = "member.txt";
            Member member = new Member();
            ObjectInputStream ois = null;
        try {

            FileInputStream fis = openFileInput(FILE_NAME1);
            ois = new ObjectInputStream(fis);
            member = (Member) ois.readObject();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        return  member;
    }


    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
