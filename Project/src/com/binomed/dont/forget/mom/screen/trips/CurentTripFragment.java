package com.binomed.dont.forget.mom.screen.trips;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.LocalActivityManagerFragment;
import com.google.android.maps.MapView;

@SuppressLint("NewApi")
public class CurentTripFragment extends LocalActivityManagerFragment {

	// @InjectView(R.id.webview)
	MapView mapView;
	View innerMapView;
	ViewGroup mapViewContainer;

	private SherlockFragmentActivity activity;
	private final static String ACTIVITY_TAG = "hosted";

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_curent_trips, container, false);
		manageMapView(mainView);
		return mainView;

	}

	private void manageMapView(View mainView) {
		mapViewContainer = (ViewGroup) mainView.findViewById(R.id.mapviewcontainer);

		Intent intent = new Intent(getActivity(), InnerMapActivity.class);
		final Window w = getLocalActivityManager().startActivity(ACTIVITY_TAG, intent);
		innerMapView = w != null ? w.getDecorView() : null;

		if (innerMapView != null) {
			ViewParent parent = innerMapView.getParent();
			if (parent != null) {
				ViewGroup v = (ViewGroup) parent;
				v.removeView(innerMapView);
			}

			innerMapView.setVisibility(View.VISIBLE);
			innerMapView.setFocusableInTouchMode(true);
			if (innerMapView instanceof ViewGroup) {
				((ViewGroup) innerMapView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			}
		}

		mapViewContainer.addView(innerMapView);

		// We retrieve the mapView
		InnerMapActivity activityMap = (InnerMapActivity) getLocalActivityManager().getActivity(ACTIVITY_TAG);
		if (activityMap != null) {
			mapView = activityMap.getMapView();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (innerMapView != null) {
			mapView.getOverlays().clear();
			mapView = null;
			mapViewContainer.removeView(innerMapView);
			innerMapView = null;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (SherlockFragmentActivity) activity;
	}

}
