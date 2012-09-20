package com.binomed.dont.forget.mom.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.binomed.dont.forget.mom.screen.trips.TripsActivity;

public class DontForgetMomActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Test
		if (true) {
			startActivity(new Intent(getApplicationContext(), TripsActivity.class));
			finish();
		}
	}

}