package petym.android.com.petym.Test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import petym.android.com.petym.ContactFragment;
import petym.android.com.petym.Intro;
import petym.android.com.petym.IntroFragment;
import petym.android.com.petym.Page;
import petym.android.com.petym.R;
import petym.android.com.petym.VO.DateItemVO;

public class Homepage_Test extends Fragment  {
    private List<Intro> introList;
    private final static String TAG = "Homepage_Test";
    private ArrayList<DateItemVO> dateItems,dateItemsImage;
    private AsyncTask dateItemTask;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private FragmentManager fragmentManager;
    private Activity activity;


    public Homepage_Test() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.homepage_test, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(fragmentManager));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(6);
        return view;

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);


            pageList = new ArrayList<>();
            pageList.add(new Page(new IntroFragment(), "推薦"));
            pageList.add(new Page(new DateFragment(), "約會"));
            pageList.add(new Page(new ContactFragment(), "即時"));

//            fragmentManager.beginTransaction();
//            fragmentManager.detach(IntroFragment.this).attach(YourFragment.this).commit();
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

}