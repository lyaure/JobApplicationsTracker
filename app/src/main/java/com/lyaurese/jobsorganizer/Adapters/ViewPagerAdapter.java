package com.lyaurese.jobsorganizer.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

// adapter for multiple fragments in the same activity or fragment
public class ViewPagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    Context context;
    ViewPager viewPager;

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
//        this.context = context;
//        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }

    // add fragment to "list" of fragments
    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
