package com.binomed.dont.forget.mom.screen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.screen.edit.EditionActivity;
import com.binomed.dont.forget.mom.screen.trips.TripsActivity;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;

public class DontForgetMomActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				Cursor trips = getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI, null, null, null, null);
				if (trips.moveToFirst()) {
					startActivity(new Intent(getApplicationContext(), TripsActivity.class));
					finish();
				} else {
					Intent editIntent = new Intent(getApplicationContext(), EditionActivity.class);
					editIntent.putExtra(DontForgetMomDbInformation.Trip._ID, -1);
					editIntent.putExtra(DontForgetMomCst.INTENT_FIRST, true);
					startActivity(editIntent);
					finish();

				}
				return null;
			}
		}.execute();
	}

}