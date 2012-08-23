package com.binomed.dont.forget.mom.screen.trips;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.PagerAdapter;
import com.binomed.dont.forget.mom.utils.AbstractActivity;
import com.viewpagerindicator.TabPageIndicator;

public class TripsActivity extends AbstractActivity {

	@InjectView(R.id.pager)
	ViewPager viewPager;

	@InjectView(R.id.indicator)
	TabPageIndicator pageIndicator;

	PagerAdapter adapter;
	ActionBar actionBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		adapter = new PagerAdapter(getSupportFragmentManager(), this);
		viewPager.setAdapter(adapter);
		pageIndicator.setViewPager(viewPager);

	}

}
