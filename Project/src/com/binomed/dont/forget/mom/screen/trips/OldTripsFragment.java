package com.binomed.dont.forget.mom.screen.trips;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.OldTripAdapter;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.utils.RoboSherlockListFragment;

public class OldTripsFragment extends RoboSherlockListFragment {

	Cursor cursor;

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_old_trips, container, false);
		((ListView) mainView.findViewById(android.R.id.list)).setEmptyView(mainView.findViewById(android.R.id.empty));
		return mainView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		requery();
	}

	public void requery() {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		cursor = getActivity().getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI, null, null, null, null);
		getListView().setAdapter(new OldTripAdapter(this //
				, cursor //
				, true));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

}
