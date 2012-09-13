package com.binomed.dont.forget.mom.screen.trips;

import android.os.Bundle;

import com.binomed.dont.forget.mom.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class InnerMapActivity extends MapActivity {

	private MapView mapView;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_map);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setClickable(false);
	}

	public MapView getMapView() {
		return mapView;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
