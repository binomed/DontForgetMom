package com.binomed.dont.forget.mom.screen.trips;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.OldTripAdapter;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.dialog.DateDialogFragment;
import com.binomed.dont.forget.mom.utils.RoboSherlockListFragment;

public class OldTripsFragment extends //
		RoboSherlockListFragment implements //
		DatePickerDialog.OnDateSetListener //
{

	Cursor cursor;
	private long tripId;

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

	public void replay(long id) {
		tripId = id;
		DateDialogFragment dateFragment = new DateDialogFragment(R.string.old_action_replay_date);
		dateFragment.setListener(this);
		dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

	}

	@Override
	public void onDateSet(DatePicker date, int year, int monthOfYear, int dayOfMonth) {
		ContentValues values = new ContentValues();
		if (tripId != -1) {
			values.put(Trip._ID, tripId);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		values.put(Trip.TRIP_DAY, calendar.getTimeInMillis());

		// Update
		String selection = DontForgetMomDbInformation.Trip._ID + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(tripId) };
		getActivity().getContentResolver().update(DontForgetMomContentProvider.CONTENT_URI, values, selection, selectionArgs);

	}

}
