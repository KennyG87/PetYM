package petym.android.com.petym;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import petym.android.com.petym.BottomEvent.Favorite;
import petym.android.com.petym.BottomEvent.Message;
import petym.android.com.petym.BottomEvent.Order;
import petym.android.com.petym.BottomEvent.Personal;
import petym.android.com.petym.Test.DateFragment;
import petym.android.com.petym.VO.Member;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private ProgressDialog progressDialog;
    private AsyncTask dateItemTask, memberTask;
    private Spinner spCategory;
    private ListView lvBook;
    private FragmentManager fragmentManager,fragmentButtonManager;
    private Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_test);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //-----------------------------------------
        fragmentManager = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(fragmentManager));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //GVSTS
        fragmentButtonManager = getSupportFragmentManager();
        ViewPager buttomPager = (ViewPager) findViewById(R.id.buttomPager);
        buttomPager.setAdapter(new MyButtonPagerAdapter(fragmentButtonManager));
        TabLayout tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);
        tabLayout1.setupWithViewPager(buttomPager);




    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new IntroFragment(), "推薦"));
            pageList.add(new Page(new DateFragment(), "約會"));
            pageList.add(new Page(new ContactFragment(), "即時"));
        }


        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }

    private class MyButtonPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageButtonList;

        public MyButtonPagerAdapter(FragmentManager fragmentManagerButton) {
            super(fragmentManagerButton);
            pageButtonList = new ArrayList<>();
            pageButtonList.add(new Page(new IntroFragment(), "Home"));
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
}

