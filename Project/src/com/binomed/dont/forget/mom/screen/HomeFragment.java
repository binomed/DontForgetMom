package com.binomed.dont.forget.mom.screen;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.screen.edit.EditionActivity;
import com.binomed.dont.forget.mom.screen.trips.TripsActivity;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;
import com.binomed.dont.forget.mom.utils.RoboSherlockFragment;

public class HomeFragment extends RoboSherlockFragment implements OnClickListener {

	@InjectView(R.id.home_btn_map)
	Button mapBtn;
	@InjectView(R.id.home_btn_new)
	Button newBtn;
	@InjectView(R.id.home_btn_about)
	Button aboutBtn;
	@InjectView(R.id.home_btn_help)
	Button helpBtn;

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mapBtn.setOnClickListener(this);
		newBtn.setOnClickListener(this);
		aboutBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);

	}

	/**
	 * Handle the click of a Feature button.
	 * 
	 * @param v
	 *            View
	 * @return void
	 */

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.home_btn_map: {
			Intent intent = new Intent(getActivity(), TripsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		}
		case R.id.home_btn_new:
			Intent intent = new Intent(getActivity(), EditionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivityForResult(intent, DontForgetMomCst.ACTIVITY_EDIT_REQUEST_CODE);
			break;
		case R.id.home_btn_about:
			// TODO
			Toast.makeText(getActivity(), "About", Toast.LENGTH_SHORT).show();
			break;
		case R.id.home_btn_help:
			// TODO
			Toast.makeText(getActivity(), "Help", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}