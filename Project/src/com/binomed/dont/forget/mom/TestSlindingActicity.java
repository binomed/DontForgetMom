package com.binomed.dont.forget.mom;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.screen.trips.CurentTripFragment;
import com.binomed.dont.forget.mom.screen.trips.OldTripsFragment;
import com.binomed.dont.forget.mom.utils.AbstractActivity;

public class TestSlindingActicity extends AbstractActivity {

	// private SlidingMenu slideMenu;
	ActionBar actionBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setBehindContentView(R.layout.view_empty_slide);
		getSupportFragmentManager().beginTransaction().add(R.id.emptySlide, new OldTripsFragment()).commit();
		setContentView(R.layout.view_empty_slide_above);
		getSupportFragmentManager().beginTransaction().add(R.id.emptySlideAbove, new CurentTripFragment()).commit();
		// slideMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		setSlidingActionBarEnabled(true);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

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

	// @Override
	// public void toggle() {
	// if (slideMenu.isBehindShowing()) {
	// showAbove();
	// } else {
	// showBehind();
	// }
	// }
	//
	// @Override
	// public void showAbove() {
	// slideMenu.showAbove();
	// }
	//
	// @Override
	// public void showBehind() {
	// slideMenu.showBehind();
	// }

}
