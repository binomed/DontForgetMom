package com.binomed.dont.forget.mom.screen.edit;

import java.text.DateFormat;
import java.util.Date;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.DontForgetMomContactAdapter;
import com.binomed.dont.forget.mom.adapter.DontForgetMomContactAdapter.TypeDatas;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.dialog.DateDialogFragment;
import com.binomed.dont.forget.mom.dialog.TimeDialogFragment;
import com.binomed.dont.forget.mom.service.contact.ContactService;
import com.binomed.dont.forget.mom.utils.AbstractActivity;

public class EditionActivity extends AbstractActivity implements OnSeekBarChangeListener, OnCheckedChangeListener {

	public static final int ITEM_SAVE = 3;

	@InjectView(R.id.tripNameTxt)
	EditText tripNameTxt;
	@InjectView(R.id.placeEdit)
	AutoCompleteTextView placeEdit;
	@InjectView(R.id.btnDate)
	Button btnDate;
	@InjectView(R.id.btnHour)
	Button btnHour;
	@InjectView(R.id.departHour)
	CheckBox departHour;
	@InjectView(R.id.seekPrecision)
	SeekBar seekPrecision;
	@InjectView(R.id.lblPrecision)
	TextView lblPrecision;
	@InjectView(R.id.radioGroupAlert)
	RadioGroup radioGroupAlert;
	@InjectView(R.id.alertSMS)
	RadioButton alertSMS;
	@InjectView(R.id.alertMail)
	RadioButton alertMail;
	@InjectView(R.id.alertMailSMS)
	RadioButton alertMailSMS;
	@InjectView(R.id.alertPhone)
	RadioButton alertPhone;
	// @InjectView(R.id.btContactRecipient)
	// ImageButton btContactRecipient;
	@InjectView(R.id.recipients)
	AutoCompleteTextView recipients;
	@InjectView(R.id.messageContent)
	EditText messageContent;

	private DateFormat timeFormat;
	private DateFormat dateFormat;

	private DontForgetMomContactAdapter adapterPlace, adapterRecipent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		timeFormat = android.text.format.DateFormat.getTimeFormat(this);
		dateFormat = android.text.format.DateFormat.getDateFormat(this);

		initDatas();
		initListeners();
	}

	private void initListeners() {
		seekPrecision.setOnSeekBarChangeListener(this);
		radioGroupAlert.setOnCheckedChangeListener(this);
	}

	private void initDatas() {

		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		long tripId = getIntent().getLongExtra(DontForgetMomDbInformation.Trip.TRIP_ID, -1);
		Cursor cursorTrip = null;
		if (tripId != -1) {
			selection = DontForgetMomDbInformation.Trip.TRIP_ID + " = ? ";
			selectionArgs = new String[] { String.valueOf(tripId) };

			cursorTrip = getContentResolver().query(DontForgetMomContentProvider.CONTENT_URI //
					, projection //
					, selection //
					, selectionArgs //
					, null // sortOrder
					);
		}

		if (cursorTrip != null && cursorTrip.moveToFirst()) {
		} else {
			tripNameTxt.setText("");
			placeEdit.setText("");
			Date today = new Date();
			btnDate.setText(dateFormat.format(today));
			btnHour.setText(timeFormat.format(today));
			seekPrecision.setProgress(50);
			lblPrecision.setText(R.string.tripPrecisionMiddle);
			alertSMS.setChecked(true);
			recipients.setText("");
			messageContent.setText("");
		}

		adapterPlace = new DontForgetMomContactAdapter(this, //
				ContactService.getContactsWithAdressCursor(getContentResolver(), null),//
				TypeDatas.ADRESS//
		);
		adapterRecipent = new DontForgetMomContactAdapter(this, //
				ContactService.getContactsWithPhoneNumberCursor(getContentResolver(), null),//
				TypeDatas.PHONE//
		);

		placeEdit.setAdapter(adapterPlace);
		recipients.setAdapter(adapterRecipent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, ITEM_SAVE, Menu.NONE, R.string.action_save) //
				.setIcon(R.drawable.ic_action_save) //
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case ITEM_SAVE:
			Intent intent = new Intent(this, EditionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	public void onClick(final View view) {
		switch (view.getId()) {
		// case R.id.btnMap:
		// break;
		// case R.id.btnContactPlace:
		// break;
		case R.id.btnDate:
			DialogFragment dateFragment = new DateDialogFragment();
			dateFragment.show(getSupportFragmentManager(), "datePicker");
			break;
		case R.id.btnHour:
			DialogFragment hourFragment = new TimeDialogFragment();
			hourFragment.show(getSupportFragmentManager(), "timePicker");
			break;
		case R.id.departHour:
			btnHour.setEnabled(departHour.isChecked());
			break;
		// case R.id.btContactRecipient:
		//
		// break;

		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		lblPrecision.requestFocus();
		if (progress < 33) {
			lblPrecision.setText(R.string.tripPrecisionMHigh);
		} else if (progress < 66) {
			lblPrecision.setText(R.string.tripPrecisionMiddle);
		} else {
			lblPrecision.setText(R.string.tripPrecisionLow);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.alertSMS:
		case R.id.alertPhone:
			adapterRecipent.setType(TypeDatas.PHONE);
			break;
		case R.id.alertMail:
			adapterRecipent.setType(TypeDatas.MAIL);
			break;
		case R.id.alertMailSMS:
			adapterRecipent.setType(TypeDatas.PHONE_MAIL);
			break;
		default:
			break;
		}

	}

}
