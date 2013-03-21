package com.binomed.dont.forget.mom.service.alarm;

import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;

public class LocalisationService extends IntentService {

	private static final String TAG = "LocalisationService";

	public LocalisationService() {
		super(LocalisationService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		long tripId = intent.getLongExtra(Trip._ID, -1L);

		// Edit preference
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = prefs.edit();
		editor.putBoolean(DontForgetMomCst.PREF_CURENT_TRIP_NOT_IN_PROGRESS, false);
		editor.putLong(DontForgetMomCst.PREF_CURENT_TRIP_IN_PROGRESS_ID, tripId);
		editor.commit();

		// We update the curent trip
		ContentValues values = new ContentValues();
		values.put(Trip.TRIP_IN_PROGRESS, 1);

		// Update
		String selection = DontForgetMomDbInformation.Trip._ID + " = ? ";
		String[] selectionArgs = new String[] { String.valueOf(tripId) };
		getContentResolver().update(DontForgetMomContentProvider.CONTENT_URI, values, selection, selectionArgs);

		// Start location service
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String[] projection = null;
		String where = Trip._ID + " = ?";
		Cursor cursor = getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI, projection, where, selectionArgs, null);

		if (cursor.moveToFirst()) {
			String place = cursor.getString(cursor.getColumnIndex(Trip.TRIP_PLACE));
			Geocoder geocoder = new Geocoder(getApplicationContext());
			if (geocoder != null) {
				List<Address> addressList = null;
				try {
					addressList = geocoder.getFromLocationName(place, 1);
					if (addressList != null && addressList.size() > 0) {
						Address address = addressList.get(0);
						Intent intentPosition = new Intent(getApplicationContext(), PositionService.class);
						intentPosition.putExtra(Trip._ID, intent.getLongExtra(Trip._ID, -1L));
						PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intentPosition, 0);
						locationManager.addProximityAlert(address.getLatitude() //
								, address.getLongitude() //
								, cursor.getInt(cursor.getColumnIndex(Trip.TRIP_PRECISION))//
								, -1 // expiration
								, pendingIntent);
					}
				} catch (Exception e) {
					Log.e(TAG, "error Searching cityName :" + place, e);
				}
			}
		}
		try {
			cursor.close();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

	}
}
