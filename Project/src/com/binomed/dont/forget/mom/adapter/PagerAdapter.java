package com.binomed.dont.forget.mom.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.binomed.dont.forget.mom.screen.trips.CurentTripFragment;
import com.viewpagerindicator.TitleProvider;

public class PagerAdapter extends FragmentPagerAdapter implements TitleProvider {

	private Fragment fragmentCurrent;
	private Fragment fragmentOlds;
	private final Context context;

	public PagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int index) {

		Fragment fragment = null;

		switch (index) {
		case 2: {
			if (fragmentOlds == null) {

				// fragmentOlds = Fragment.instantiate(context, fname)
			}
			fragment = fragmentOlds;
		}
		default: {
			if (fragmentCurrent == null) {
				fragmentCurrent = new CurentTripFragment();
			}
			fragment = fragmentCurrent;
		}
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public String getTitle(int position) {
		return "Page " + position;
	}

}
