package com.binomed.dont.forget.mom.service.alarm;

import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;

import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;

public class LocalisationService extends IntentService {

	private static final String TAG = "LocalisationService";

	public LocalisationService() {
		super(LocalisationService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String[] projection = null;
		String[] selectionArgs = new String[] { String.valueOf(intent.getLongExtra(Trip._ID, -1L)) };
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

	}
}
