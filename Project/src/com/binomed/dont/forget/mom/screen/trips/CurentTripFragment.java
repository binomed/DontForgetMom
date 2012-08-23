package com.binomed.dont.forget.mom.screen.trips;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.RoboSherlockFragment;

public class CurentTripFragment extends RoboSherlockFragment {

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_curent_trips, container, false);

		return mainView;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

}
