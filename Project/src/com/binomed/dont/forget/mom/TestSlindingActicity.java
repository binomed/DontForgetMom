package com.binomed.dont.forget.mom;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.binomed.dont.forget.mom.screen.trips.CurentTripFragment;
import com.binomed.dont.forget.mom.utils.AbstractActivity;

public class TestSlindingActicity extends AbstractActivity {

	// private SlidingMenu slideMenu;
	ActionBar actionBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_empty_slide_above);
		getSupportFragmentManager().beginTransaction().add(R.id.emptySlideAbove, new CurentTripFragment()).commit();
		// slideMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		setSlidingActionBarEnabled(true);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

}
