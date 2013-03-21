package com.binomed.dont.forget.mom.service.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;

public class BootService extends IntentService {

	public BootService() {
		super(BootService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Calendar date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_MONTH, -1);
		String[] projection = null;
		String[] selectionArgs = new String[] { String.valueOf(date.getTimeInMillis()) };
		String where = Trip.TRIP_DAY + " > ?";
		Cursor cursor = getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI, projection, where, selectionArgs, null);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intentService = null;
		PendingIntent pendingIntent = null;
		while (cursor.moveToNext()) {
			date.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(Trip.TRIP_DAY)));
			intentService = new Intent(getApplicationContext(), LocalisationService.class);
			intentService.putExtra(Trip._ID, cursor.getLong(cursor.getColumnIndex(Trip._ID)));
			pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intentService, 0);
			alarm.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
		}
		try {
			cursor.close();
		} catch (Exception e) {
			Log.e(BootService.class.getName(), e.getMessage(), e);
		}

	}
}
