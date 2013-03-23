package com.binomed.dont.forget.mom.screen.trips;

import java.io.IOException;
import java.util.List;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CurentTripFragment extends SherlockFragment implements LocationListener, OnClickListener {

	View mainView;
	Marker curentPosition;
	RelativeLayout mask;

	TextView tripName;

	SupportMapFragment mapFragment;
	SharedPreferences prefs;
	Address address;

	private SherlockFragmentActivity activity;
	private final static String ACTIVITY_TAG = "hosted";
	private final static String TAG = "CurentTripFramgent";

	private static final int ACTION_SMS = 0;
	private static final int ACTION_MAIL = 1;
	private static final int ACTION_CALL = 2;

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mainView = inflater.inflate(R.layout.fragment_curent_trips, container, false);
		manageMapView(mainView);

		TextView imgSms = (TextView) mainView.findViewById(R.id.imgSms);
		TextView imgMail = (TextView) mainView.findViewById(R.id.imgMails);
		TextView imgCall = (TextView) mainView.findViewById(R.id.imgCall);
		mask = (RelativeLayout) mainView.findViewById(R.id.mask);

		imgSms.setOnClickListener(this);
		imgMail.setOnClickListener(this);
		imgCall.setOnClickListener(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		manageMask(prefs.getBoolean(DontForgetMomCst.PREF_CURENT_TRIP_NOT_IN_PROGRESS, true));

		tripName = (TextView) mainView.findViewById(R.id.tripName);

		return mainView;

	}

	public void manageMask(boolean show) {
		mask.setVisibility(show ? View.VISIBLE : View.GONE);
		mainView.findViewById(R.id.scrollView).setVisibility(!show ? View.VISIBLE : View.GONE);
		if (!show) {
			fillViews();
		}
	}

	public void fillViews() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String selection = Trip._ID + " = ?";
				String[] args = new String[] { String.valueOf(prefs.getLong(DontForgetMomCst.PREF_CURENT_TRIP_IN_PROGRESS_ID, -1L)) };
				Cursor cursor = getActivity().getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI, null, selection, args, null);
				String result = null;
				if (cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(Trip.TRIP_NAME));
					Geocoder geocoder = new Geocoder(getActivity());
					List<Address> addresses = null;

					try {
						// Getting a maximum of 3 Address that matches the input text
						addresses = geocoder.getFromLocationName(cursor.getString(cursor.getColumnIndex(Trip.TRIP_PLACE)), 1);
						if (addresses != null && addresses.size() > 0) {
							address = addresses.get(0);
						}
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
					}
					try {
						cursor.close();
					} catch (Exception e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}

				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					return;
				}
				tripName.setText(result);

				if (address != null) {
					LatLng geoPoint = new LatLng(address.getLatitude(), address.getLongitude());
					curentPosition.setPosition(geoPoint);
					mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(geoPoint, 15));
				}
			}

		}.execute();

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ScrollView scrollView = (ScrollView) mainView.findViewById(R.id.scrollView);
		scrollView.smoothScrollTo(0, 0);
	}

	private void manageMapView(View mainView) {
		mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapFragment); // (SupportMapFragment) mainView.findViewById(R.id.mapFragment);
		curentPosition = mapFragment.getMap().addMarker(new MarkerOptions() //
				.position(new LatLng(0, 0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_indicator_current_position)));

		mapFragment.getMap().animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		onLocationChanged(getLastLocation(getActivity()));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		unRegisterListener(getActivity(), this);

	}

	@Override
	public void onResume() {
		super.onResume();
		registerLocalisationListener(getActivity(), this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (SherlockFragmentActivity) activity;
	}

	/*
	 * 
	 * 
	 * Locations management
	 */

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			LatLng geoPoint = new LatLng(location.getLatitude(), location.getLongitude());
			curentPosition.setPosition(geoPoint);
			mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(geoPoint, 15));
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// nothing to do

	}

	@Override
	public void onProviderEnabled(String provider) {
		// nothing to do

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// nothing to do

	}

	private boolean isLocalisationEnabled(Context context) {
		boolean result = false;
		LocationManager locationManager = getLocationManager(context);
		if (locationManager != null) {
			try {
				result = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			} catch (Exception e) {
				result = false;
			}
		}
		return result;
	}

	private LocationManager getLocationManager(Context context) {
		return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	private Location getLastLocation(Context context) {
		Location result = null;
		if (isLocalisationEnabled(context)) {
			LocationManager locationManager = getLocationManager(context);
			if (locationManager != null) {
				result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		return result;
	}

	public void registerLocalisationListener(final Context context, final LocationListener listener) {
		if (isLocalisationEnabled(context)) {
			LocationManager locationManager = getLocationManager(context);
			if (locationManager != null) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, listener);
			} else {
				if (Log.isLoggable(TAG, Log.DEBUG)) {
					Log.d(TAG, "No listener Put"); //$NON-NLS-1$
					listener.onProviderDisabled(null);
				}
			}
		} else {
			listener.onProviderDisabled(null);
		}
	}

	public void unRegisterListener(Context context, LocationListener listener) {
		LocationManager locationManager = getLocationManager(context);
		if (locationManager != null) {
			locationManager.removeUpdates(listener);
		} else {
			if (Log.isLoggable(TAG, Log.DEBUG)) {
				Log.d(TAG, "No listener Put"); //$NON-NLS-1$
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imgCall:
			// TODO mettre l'icone de la personne avec le nom dessous
			ActionItem callActionItem = new ActionItem(ACTION_CALL, getActivity().getResources().getDrawable(R.drawable.ic_about2_focus));
			QuickAction quickAction = new QuickAction(getActivity());
			quickAction.addActionItem(callActionItem);
			quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {

				@Override
				public void onItemClick(QuickAction source, int pos, int actionId) {
					// TODO Auto-generated method stub

				}
			});
			quickAction.show(view);
			break;
		case R.id.imgMails:
		case R.id.imgSms:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle(R.string.dialogConfirmation);
			int pluralId = R.plurals.dialogConfirmationEmail;
			if (view.getId() == R.id.imgSms) {
				pluralId = R.plurals.dialogConfirmationSms;
			}
			alertDialog.setMessage(getResources().getQuantityString(pluralId, 1, null));
			alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			AlertDialog dialog = alertDialog.create();
			dialog.show();
			break;

		default:
			break;
		}

	}
}
