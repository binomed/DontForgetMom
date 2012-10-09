package com.binomed.dont.forget.mom.screen.trips;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.DontForgetMomPagerAdapter;
import com.binomed.dont.forget.mom.screen.edit.EditionActivity;
import com.binomed.dont.forget.mom.utils.AbstractActivity;
import com.viewpagerindicator.TabPageIndicator;

public class TripsActivity extends AbstractActivity {

	@InjectView(R.id.pager)
	ViewPager viewPager;

	@InjectView(R.id.indicator)
	TabPageIndicator pageIndicator;

	DontForgetMomPagerAdapter adapter;
	ActionBar actionBar;

	private static final int ITEM_NEW = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_trips);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		adapter = new DontForgetMomPagerAdapter(getSupportFragmentManager(), this);
		viewPager.setAdapter(adapter);
		pageIndicator.setViewPager(viewPager, 1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, ITEM_NEW, 1, R.string.action_new) //
				.setIcon(R.drawable.ic_action_new) //
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case ITEM_NEW:
			Intent intent = new Intent(this, EditionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

}
