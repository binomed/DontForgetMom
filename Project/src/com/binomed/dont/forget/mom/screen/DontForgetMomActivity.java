package com.binomed.dont.forget.mom.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.TestSlindingActicity;
import com.binomed.dont.forget.mom.screen.edit.EditionActivity;
import com.binomed.dont.forget.mom.screen.trips.TripsActivity;
import com.binomed.dont.forget.mom.utils.AbstractActivity;

public class DontForgetMomActivity extends AbstractActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	/**
	 * Handle the click of a Feature button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */

	public void onClickFeature(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.home_btn_map:
			startActivity(new Intent(getApplicationContext(), TripsActivity.class));
			break;
		case R.id.home_btn_new:
			startActivity(new Intent(getApplicationContext(), EditionActivity.class));
			break;
		case R.id.home_btn_about:
			startActivity(new Intent(getApplicationContext(), TestSlindingActicity.class));
			// TODO
			Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
			break;
		case R.id.home_btn_help:
			// TODO
			Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}