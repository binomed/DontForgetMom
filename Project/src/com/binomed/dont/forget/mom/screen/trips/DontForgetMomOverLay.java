package com.binomed.dont.forget.mom.screen.trips;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class DontForgetMomOverLay extends ItemizedOverlay<OverlayItem> {

	List<GeoPoint> points = new ArrayList<GeoPoint>();

	public DontForgetMomOverLay(Drawable arg0) {
		super(boundCenter(arg0));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		GeoPoint point = points.get(arg0);
		return new OverlayItem(point, "Titre", "Description");
	}

	@Override
	public int size() {
		return points.size();
	}

	public void managePoint(GeoPoint point) {
		points.clear();
		points.add(point);
		populate();
	}

}
