package com.example.a22607.show.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a22607.show.mainfragment.HomeFragment;
import com.example.a22607.show.mainfragment.MainFragment;
import com.example.a22607.show.mainfragment.SearchFragment;

/**
 * Created by 22607 on 2017/2/25.
 */

public class MainAdapter extends FragmentPagerAdapter {
    private String[] mTitles=new String[]{"首页","发现","用户"};
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return  new MainFragment();
        }else  if(position==1){
            return new SearchFragment();
        }else if(position==2){
            return  new HomeFragment();
        }
        return new MainFragment();
    }

    @Override
    public int getCount() {
        return  mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
