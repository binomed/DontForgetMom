package com.binomed.dont.forget.mom.service.alarm;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;

public class PositionService extends IntentService {

	public PositionService() {
		super(PositionService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		long tripId = intent.getLongExtra(Trip._ID, -1L);
		Cursor cursor = getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI //
				, null // projection
				, Trip._ID + "=?" // selection
				, new String[] { String.valueOf(tripId) } // args
				, null // sort
				);
		if (cursor.moveToFirst()) {

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			if (prefs.getBoolean(getString(R.string.pref_automatic_send_key), true)) {
				// Do action
				// TODO gerer cas envoie auto
			} else {
				// Notification send alert
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
					// TODO affichage des notifications

				} else {
					// TODO affichage des notifications

				}
			}

			// We update the curent trip
			ContentValues values = new ContentValues();
			values.put(Trip.TRIP_IN_PROGRESS, 1);

			// Update
			String selection = DontForgetMomDbInformation.Trip._ID + " = ? ";
			String[] selectionArgs = new String[] { String.valueOf(tripId) };
			getContentResolver().update(DontForgetMomContentProvider.CONTENT_URI, values, selection, selectionArgs);
			prefs.edit().putBoolean(DontForgetMomCst.PREF_CURENT_TRIP_NOT_IN_PROGRESS, true).commit();

		}

		try {
			cursor.close();
		} catch (Exception e) {
			Log.e(PositionService.class.getName(), e.getMessage(), e);
		}

	}

}
