package com.binomed.dont.forget.mom.adapter;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.screen.trips.OldTripsFragment;
import com.binomed.dont.forget.mom.view.OldTripView;

public class OldTripAdapter extends CursorAdapter {

	OldTripsFragment fragment;

	public OldTripAdapter(OldTripsFragment context, Cursor c, boolean autoRequery) {
		super(context.getActivity(), c, autoRequery);
		fragment = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		OldTripView oldTrip = (OldTripView) view;
		fillView(oldTrip, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		OldTripView view = new OldTripView(context, this);
		fillView(view, cursor);
		return view;
	}

	public void requery() {
		fragment.requery();
	}

	private void fillView(OldTripView view, Cursor cursor) {
		Date lastLaunch = null;
		if (cursor.getLong(cursor.getColumnIndex(Trip.TRIP_LAST_LAUNCH)) != -1l) {
			lastLaunch = new Date(cursor.getLong(cursor.getColumnIndex(Trip.TRIP_LAST_LAUNCH)));
		}
		view.setTrip(cursor.getInt(cursor.getColumnIndex(Trip._ID)) //
				, cursor.getString(cursor.getColumnIndex(Trip.TRIP_NAME)) //
				, lastLaunch //
				, cursor.getShort(cursor.getColumnIndex(Trip.TRIP_IN_PROGRESS)) == 1 //
		);
	}

	public void replay(int tripId) {
		fragment.replay(tripId);

	}
}
