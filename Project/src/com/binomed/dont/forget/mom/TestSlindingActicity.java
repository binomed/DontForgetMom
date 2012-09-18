package com.binomed.dont.forget.mom;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.screen.trips.CurentTripFragment;
import com.binomed.dont.forget.mom.screen.trips.OldTripsFragment;
import com.binomed.dont.forget.mom.utils.AbstractActivity;
import com.slidingmenu.lib.SlidingMenu;

public class TestSlindingActicity extends AbstractActivity {

	private SlidingMenu slideMenu;
	ActionBar actionBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide);

		slideMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		getSupportFragmentManager().beginTransaction().add(R.id.emptySlideAbove, new CurentTripFragment()).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.emptySlide, new OldTripsFragment()).commit();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	public void toggle() {
		if (slideMenu.isBehindShowing()) {
			showAbove();
		} else {
			showBehind();
		}
	}

	public void showAbove() {
		slideMenu.showAbove();
	}

	public void showBehind() {
		slideMenu.showBehind();
	}

}
