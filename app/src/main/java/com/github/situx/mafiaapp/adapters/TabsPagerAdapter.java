package com.github.situx.mafiaapp.adapters;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;
        import com.github.situx.mafiaapp.views.fragment.Round2Fragment;

        import java.util.*;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    public static List<Round2Fragment> tabs;

    public static Map<Integer,Bundle> bundlemap;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        tabs=new LinkedList<>();
        bundlemap=new HashMap<>();
    }

    public void addFragment(Round2Fragment fragment, Integer position, Bundle bundle){
        tabs.add(fragment);
        bundlemap.put(tabs.size()-1,bundle);
        this.notifyDataSetChanged();
    }

    public void removeFragment(Round2Fragment fragment){
        tabs.remove(fragment);
        this.notifyDataSetChanged();
    }

    public Fragment getFragment(Integer position){
        return tabs.get(position);
    }

    @Override
    public Fragment getItem(int index) {
        Round2Fragment round2= tabs.get(index);
        round2.setArguments(bundlemap.get(index));
        return round2;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return tabs.size();
    }

}
