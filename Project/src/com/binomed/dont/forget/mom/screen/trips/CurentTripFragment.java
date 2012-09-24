package com.binomed.dont.forget.mom.screen.trips;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.LocalActivityManagerFragment;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

@SuppressLint("NewApi")
public class CurentTripFragment extends LocalActivityManagerFragment implements LocationListener, OnClickListener {

	MapView mapView;
	MapController mapController;
	View innerMapView, mainView;
	ViewGroup mapViewContainer;
	DontForgetMomOverLay overLay;

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

		imgSms.setOnClickListener(this);
		imgMail.setOnClickListener(this);
		imgCall.setOnClickListener(this);

		return mainView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ScrollView scrollView = (ScrollView) mainView.findViewById(R.id.scrollView);
		scrollView.smoothScrollTo(0, 0);
	}

	private void manageMapView(View mainView) {
		mapViewContainer = (ViewGroup) mainView.findViewById(R.id.mapviewcontainer);

		Intent intent = new Intent(getActivity(), InnerMapActivity.class);
		final Window w = getLocalActivityManager().startActivity(ACTIVITY_TAG, intent);
		innerMapView = w != null ? w.getDecorView() : null;

		if (innerMapView != null) {
			ViewParent parent = innerMapView.getParent();
			if (parent != null) {
				ViewGroup v = (ViewGroup) parent;
				v.removeView(innerMapView);
			}

			innerMapView.setVisibility(View.VISIBLE);
			innerMapView.setFocusableInTouchMode(true);
			if (innerMapView instanceof ViewGroup) {
				((ViewGroup) innerMapView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			}
		}

		mapViewContainer.addView(innerMapView);

		// We retrieve the mapView
		InnerMapActivity activityMap = (InnerMapActivity) getLocalActivityManager().getActivity(ACTIVITY_TAG);
		if (activityMap != null) {
			mapView = activityMap.getMapView();
			mapController = mapView.getController();
			mapController.setZoom(17);

			overLay = new DontForgetMomOverLay(getResources().getDrawable(R.drawable.ic_maps_indicator_current_position));
			mapView.getOverlays().add(overLay);

			onLocationChanged(getLastLocation(getActivity()));

		}
	}

	private int microdefress(double value) {
		return (int) (value * 1000000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (innerMapView != null) {
			mapView.getOverlays().clear();
			mapView = null;
			mapViewContainer.removeView(innerMapView);
			innerMapView = null;
		}
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
			GeoPoint geoPoint = new GeoPoint(microdefress(location.getLatitude()), microdefress(location.getLongitude()));
			mapController.animateTo(geoPoint);
			overLay.managePoint(geoPoint);
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
