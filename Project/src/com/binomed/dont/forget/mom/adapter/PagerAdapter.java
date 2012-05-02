package com.binomed.dont.forget.mom.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

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

}
