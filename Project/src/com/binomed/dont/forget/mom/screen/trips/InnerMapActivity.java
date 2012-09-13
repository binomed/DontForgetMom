package com.binomed.dont.forget.mom.screen.trips;

import android.os.Bundle;

import com.binomed.dont.forget.mom.R;
import com.google.android.maps.MapActivity;

public class InnerMapActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_map);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
