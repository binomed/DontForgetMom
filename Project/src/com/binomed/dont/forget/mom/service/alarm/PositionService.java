package com.binomed.dont.forget.mom.service.alarm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;

public class PositionService extends IntentService {

	public PositionService() {
		super(PositionService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Cursor cursor = getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI //
				, null // projection
				, Trip._ID + "=?" // selection
				, new String[] { String.valueOf(intent.getLongExtra(Trip._ID, -1L)) } // args
				, null // sort
				);
		if (cursor.moveToFirst()) {

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			if (prefs.getBoolean(getString(R.string.pref_automatic_send_key), true)) {
				// Do action
			} else {
				// Notification send alert
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {

				} else {

				}
			}

		}

		try {
			cursor.close();
		} catch (Exception e) {
			Log.e(PositionService.class.getName(), e.getMessage(), e);
		}

	}

}
