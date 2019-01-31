package com.example.klost.lolstats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SavedProfileViewPagerAdapter extends FragmentPagerAdapter {

    private int entryId;
    public final String SAVED_ENTRY = "SAVED_ENTRY_ID";

    public SavedProfileViewPagerAdapter(FragmentManager fm, int entryId) {
        super(fm);
        this.entryId = entryId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(SAVED_ENTRY, entryId);
        switch (position) {
            case 0:
                FragmentFavProfileSummary fragment = new FragmentFavProfileSummary();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                FragmentFavProfileChampionsStats fragment2 = new FragmentFavProfileChampionsStats();
                fragment2.setArguments(bundle);
                return fragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = "Summary";
                break;
            case 1:
                title = "Champion Stats";
                break;
        }

        return title;
    }

}
