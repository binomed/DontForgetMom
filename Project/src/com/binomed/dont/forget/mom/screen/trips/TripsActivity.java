package com.binomed.dont.forget.mom.screen.trips;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.AbstractActivity;
import com.viewpagerindicator.PageIndicator;

public class TripsActivity extends AbstractActivity {

	@InjectView(R.id.pager)
	ViewPager viewPager;

	@InjectView(R.id.indicator)
	PageIndicator pageIndicator;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

}
