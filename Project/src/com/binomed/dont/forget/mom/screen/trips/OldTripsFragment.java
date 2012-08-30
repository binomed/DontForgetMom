package com.binomed.dont.forget.mom.screen.trips;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.RoboSherlockListFragment;

public class OldTripsFragment extends RoboSherlockListFragment {

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_old_trips, container, false);

		// TextView emptyView = new TextView(getActivity());
		// emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// emptyView.setTextColor(getResources().getColor(android.R.color.black));
		// emptyView.setText(R.string.no_trips);
		// emptyView.setTextSize(20);
		// emptyView.setVisibility(View.GONE);
		// emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		// ((ListView) mainView.findViewById(android.R.id.list)).setEmptyView(inflater.inflate(R.layout.view_empty_trips, container));
		((ListView) mainView.findViewById(android.R.id.list)).setEmptyView(mainView.findViewById(android.R.id.empty));

		return mainView;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

}
