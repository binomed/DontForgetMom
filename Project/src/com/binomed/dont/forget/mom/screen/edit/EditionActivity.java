package com.binomed.dont.forget.mom.screen.edit;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import roboguice.inject.InjectView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.DontForgetMomContactAdapter;
import com.binomed.dont.forget.mom.adapter.DontForgetMomContactAdapter.TypeDatas;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.dialog.DateDialogFragment;
import com.binomed.dont.forget.mom.dialog.TimeDialogFragment;
import com.binomed.dont.forget.mom.service.contact.ContactService;
import com.binomed.dont.forget.mom.utils.AbstractActivity;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;

public class EditionActivity extends AbstractActivity //
		implements OnSeekBarChangeListener //
		, OnCheckedChangeListener //
		, DatePickerDialog.OnDateSetListener //
		, TimePickerDialog.OnTimeSetListener //
{

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
	@InjectView(R.id.recipients)
	MultiAutoCompleteTextView recipients;
	@InjectView(R.id.messageContent)
	EditText messageContent;

	private Date date = new Date(), hour = new Date();
	private int tripId;

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

		seekPrecision.setFocusable(true);
		seekPrecision.setFocusableInTouchMode(true);

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
		tripId = getIntent().getIntExtra(DontForgetMomDbInformation.Trip._ID, -1);
		Cursor cursorTrip = null;
		if (tripId != -1) {
			selection = DontForgetMomDbInformation.Trip._ID + " = ? ";
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
		recipients.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}

	private boolean validateDatas() {
		boolean result = true;

		if (tripNameTxt.getText().toString().trim().length() == 0) {
			result = false;
			// TODO gerer erreur
		} else if (placeEdit.getText().toString().trim().length() == 0) {
			result = false;
			// TODO gerer erreur
		} else if (recipients.getText().toString().trim().length() == 0) {
			result = false;
			// TODO gerer erreur
		} else if (messageContent.getText().toString().trim().length() == 0) {
			result = false;
			// TODO gerer erreur
		} else if (radioGroupAlert.getCheckedRadioButtonId() == R.id.alertPhone //
				&& recipients.getText().toString().trim().indexOf(",") != recipients.getText().toString().trim().lastIndexOf(",")) {
			result = false;
			// On ne peut pas appeler plusieurs personnes.
			// TODO gerer erreur
		}

		return result;
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
			if (validateDatas()) {
				ContentValues values = new ContentValues();
				if (tripId != -1) {
					values.put(Trip._ID, tripId);
				}
				values.put(Trip.TRIP_NAME, tripNameTxt.getText().toString());
				values.put(Trip.TRIP_PLACE, placeEdit.getText().toString());
				values.put(Trip.TRIP_RECIPIENT, recipients.getText().toString());
				switch (radioGroupAlert.getCheckedRadioButtonId()) {
				case R.id.alertSMS:
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_SMS);
					break;
				case R.id.alertMail:
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_MAIL);

					break;
				case R.id.alertMailSMS:
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_SMS_MAIL);

					break;
				case R.id.alertPhone:
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_PHONE);
					break;
				default:
					break;
				}
				if (seekPrecision.getProgress() < 33) {
					values.put(Trip.TRIP_PRECISION, DontForgetMomCst.PRECISION_HIGH);
				} else if (seekPrecision.getProgress() < 66) {
					values.put(Trip.TRIP_PRECISION, DontForgetMomCst.PRECISION_NORMAL);
				} else {
					values.put(Trip.TRIP_PRECISION, DontForgetMomCst.PRECISION_LOW);
				}
				values.put(Trip.TRIP_MESSAGE, messageContent.getText().toString());
				values.put(Trip.TRIP_DAY, date.getTime());
				if (btnHour.isEnabled()) {
					values.put(Trip.TRIP_HOUR, hour.getTime());
				}

				if (tripId != -1) {
					// Update
					String selection = DontForgetMomDbInformation.Trip._ID + " = ? ";
					String[] selectionArgs = new String[] { String.valueOf(tripId) };
					getContentResolver().update(DontForgetMomContentProvider.CONTENT_URI, values, selection, selectionArgs);
				} else {
					// New
					getContentResolver().insert(DontForgetMomContentProvider.CONTENT_URI, values);

				}

				finish();
			}
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	public void onClick(final View view) {
		switch (view.getId()) {
		case R.id.btnDate:
			DateDialogFragment dateFragment = new DateDialogFragment();
			dateFragment.setListener(this);
			dateFragment.show(getSupportFragmentManager(), "datePicker");
			break;
		case R.id.btnHour:
			DialogFragment hourFragment = new TimeDialogFragment();
			hourFragment.show(getSupportFragmentManager(), "timePicker");
			break;
		case R.id.departHour:
			btnHour.setEnabled(departHour.isChecked());
			break;

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
		seekPrecision.requestFocus();
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

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		date = calendar.getTime();

	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		hour = calendar.getTime();

	}

}
