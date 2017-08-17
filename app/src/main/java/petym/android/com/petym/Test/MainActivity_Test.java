package petym.android.com.petym.Test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import petym.android.com.petym.BottomEvent.Favorite;
import petym.android.com.petym.BottomEvent.Message;
import petym.android.com.petym.BottomEvent.Order;
import petym.android.com.petym.BottomEvent.Personal;

import petym.android.com.petym.IntroFragment;
import petym.android.com.petym.MainActivity;
import petym.android.com.petym.Page;
import petym.android.com.petym.R;
import petym.android.com.petym.VO.Member;
import petym.android.com.petym.aLogin.Common;

public class MainActivity_Test extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private ProgressDialog progressDialog;
    private AsyncTask dateItemTask, memberTask;
    private Spinner spCategory;
    private ListView lvBook;
    private FragmentManager fragmentManager,fragmentButtonManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private final static String FILE_NAME1 = "member.txt";
    public static Member member=null;
    private Common common;
    private ObjectInputStream ois = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_test);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //GVSTS
        fragmentButtonManager = getSupportFragmentManager();
        ViewPager buttomPager = (ViewPager) findViewById(R.id.buttomPager);
        buttomPager.setAdapter(new MyButtonPagerAdapter(fragmentButtonManager));
        TabLayout tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);
        tabLayout1.setupWithViewPager(buttomPager);
        buttomPager.setOffscreenPageLimit(4);


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

        Log.d("cool","cool::::"+member.getMemName());
    }





    private class MyButtonPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageButtonList;


        public MyButtonPagerAdapter(FragmentManager fragmentManagerButton) {
            super(fragmentManagerButton);

            pageButtonList = new ArrayList<>();
            pageButtonList.add(new Page(new Homepage_Test(), "Home"));
            pageButtonList.add(new Page(new Order(), "Order"));
            pageButtonList.add(new Page(new Favorite(), "Favorite"));
            pageButtonList.add(new Page(new Message(), "Message"));
            pageButtonList.add(new Page(new Personal(), "Me"));

        }


        @Override
        public Fragment getItem(int position) {
            return pageButtonList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageButtonList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageButtonList.get(position).getTitle();
        }
    }


//    private class MyPagerAdapter extends FragmentPagerAdapter {
//        List<Page> pageList;
//
//        public MyPagerAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//            pageList = new ArrayList<>();
//            pageList.add(new Page(new IntroFragment(), "推薦"));
//            pageList.add(new Page(new DateFragment(), "約會"));
//            pageList.add(new Page(new ContactFragment(), "即時"));
//        }
//
//
//        @Override
//        public Fragment getItem(int position) {
//            return pageList.get(position).getFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return pageList.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return pageList.get(position).getTitle();
//        }
//    }
}

