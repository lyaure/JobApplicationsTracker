package com.lyaurese.jobapplicationstracker.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

// adapter for multiple fragments in the same activity or fragment
public class ViewPagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

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
