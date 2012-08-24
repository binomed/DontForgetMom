package com.binomed.dont.forget.mom.adapter;

import roboguice.RoboGuice;
import roboguice.inject.InjectResource;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.screen.trips.CurentTripFragment;
import com.binomed.dont.forget.mom.screen.trips.OldTripsFragment;
import com.viewpagerindicator.TitleProvider;

public class DontForgetMomPagerAdapter extends FragmentPagerAdapter implements TitleProvider {

	private static final String TAG = "DontForgetMomAdapter";

	private static final int NB_PAGES = 2;

	private Fragment fragmentCurrent;
	private Fragment fragmentOlds;
	private final Context context;
	@InjectResource(R.string.tab_old)
	String oldTabName;
	@InjectResource(R.string.tab_current)
	String curentTabName;

	public DontForgetMomPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
		RoboGuice.getInjector(context).injectMembers(this);
	}

	@Override
	public Fragment getItem(int index) {
		Fragment fragment = null;
		switch (index) {
		case 0: {
			if (fragmentOlds == null) {
				fragmentOlds = Fragment.instantiate(context, OldTripsFragment.class.getName());
			}
			fragment = fragmentOlds;
			break;
		}
		default: {
			if (fragmentCurrent == null) {
				fragmentCurrent = Fragment.instantiate(context, CurentTripFragment.class.getName());
			}
			fragment = fragmentCurrent;
			break;
		}
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return NB_PAGES;
	}

	@Override
	public String getTitle(int position) {
		switch (position) {
		case 0:
			return oldTabName;
		default:
			return curentTabName;
		}
	}

}
