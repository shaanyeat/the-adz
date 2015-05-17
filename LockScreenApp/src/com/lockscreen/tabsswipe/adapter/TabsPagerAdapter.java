package com.lockscreen.tabsswipe.adapter;

import com.lockscreen.fragment.CampaignFragment;
import com.lockscreen.fragment.HomeFragment;
import com.lockscreen.fragment.RecordsFragment;
import com.lockscreen.fragment.RankingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Home fragment activity
			return new HomeFragment();
		case 1:
			// Redeem fragment activity
			return new CampaignFragment();
		case 2:
			// Records fragment activity
			return new RecordsFragment();
		case 3:
			// Ranking fragment activity
			return new RankingFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
