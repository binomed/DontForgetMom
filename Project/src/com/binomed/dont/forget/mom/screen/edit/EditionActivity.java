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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.DontForgetMomContactAdapter;
import com.binomed.dont.forget.mom.adapter.DontForgetMomContactAdapter.TypeDatas;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.dialog.DateDialogFragment;
import com.binomed.dont.forget.mom.service.contact.ContactService;
import com.binomed.dont.forget.mom.utils.AbstractDontForgetMomActivity;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;

public class EditionActivity extends AbstractDontForgetMomActivity //
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
	@InjectView(R.id.seekPrecision)
	SeekBar seekPrecision;
	@InjectView(R.id.lblPrecision)
	TextView lblPrecision;
	@InjectView(R.id.alertSMS)
	CheckBox alertSMS;
	@InjectView(R.id.alertMail)
	CheckBox alertMail;
	@InjectView(R.id.alertPhone)
	CheckBox alertPhone;
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
		actionBar.setDisplayHomeAsUpEnabled(false);
		timeFormat = android.text.format.DateFormat.getTimeFormat(this);
		dateFormat = android.text.format.DateFormat.getDateFormat(this);

		seekPrecision.setFocusable(true);
		seekPrecision.setFocusableInTouchMode(true);

		initDatas();
		initListeners();
	}

	private void initListeners() {
		seekPrecision.setOnSeekBarChangeListener(this);
		alertSMS.setOnCheckedChangeListener(this);
		alertPhone.setOnCheckedChangeListener(this);
		alertMail.setOnCheckedChangeListener(this);
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
			tripNameTxt.setText(cursorTrip.getString(cursorTrip.getColumnIndex(Trip.TRIP_NAME)));
			placeEdit.setText(cursorTrip.getString(cursorTrip.getColumnIndex(Trip.TRIP_PLACE)));
			Date today = new Date(cursorTrip.getLong(cursorTrip.getColumnIndex(Trip.TRIP_DAY)));
			btnDate.setText(dateFormat.format(today));
			int precision = cursorTrip.getInt(cursorTrip.getColumnIndex(Trip.TRIP_PRECISION));
			seekPrecision.setProgress(precision);
			showPrecision(precision);
			recipients.setText(cursorTrip.getString(cursorTrip.getColumnIndex(Trip.TRIP_RECIPIENT)));
			messageContent.setText(cursorTrip.getString(cursorTrip.getColumnIndex(Trip.TRIP_MESSAGE)));

			cursorTrip.close();
		} else {
			tripNameTxt.setText("");
			placeEdit.setText("");
			Date today = new Date();
			btnDate.setText(dateFormat.format(today));
			seekPrecision.setProgress(50);
			showPrecision(50);
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

		if (tripNameTxt.getVisibility() != View.GONE && tripNameTxt.getText().toString().trim().length() == 0) {
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
		} else if (alertPhone.isChecked() //
				&& recipients.getText().toString().trim().indexOf(",") != recipients.getText().toString().trim().lastIndexOf(",")) {
			result = false;
			// On ne peut pas appeler plusieurs personnes.
			// TODO gerer erreur
		}

		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_edit_menu, menu);

		/** Get the action view of the menu item whose id is search */
		final View v = (View) menu.findItem(R.id.ic_action_name).getActionView();

		/** Get the edit text from the action view */
		EditText txtSearch = (EditText) v.findViewById(R.id.name_trip);

		/** Setting an action listener */
		txtSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				v.setText(v.getText());
				Toast.makeText(getBaseContext(), "Search : " + v.getText(), Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.ic_action_save:
			if (validateDatas()) {
				ContentValues values = new ContentValues();
				if (tripId != -1) {
					values.put(Trip._ID, tripId);
				}
				if (tripNameTxt.getVisibility() == View.GONE) {

					values.put(Trip.TRIP_NAME, placeEdit.getText().toString());
				} else {
					values.put(Trip.TRIP_NAME, tripNameTxt.getText().toString());
				}
				values.put(Trip.TRIP_PLACE, placeEdit.getText().toString());
				values.put(Trip.TRIP_RECIPIENT, recipients.getText().toString());
				if (alertSMS.isChecked() && alertMail.isChecked()) {
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_SMS_MAIL);
				} else if (alertSMS.isChecked()) {
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_SMS);
				} else if (alertMail.isChecked()) {
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_MAIL);
				} else {
					values.put(Trip.TRIP_TYPE_ALERT, DontForgetMomCst.TYPE_ALERT_PHONE);
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
		case R.id.ic_action_name:
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

		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		lblPrecision.requestFocus();
		showPrecision(progress);
	}

	private void showPrecision(int progress) {
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.alertPhone:
			if (isChecked) {
				alertMail.setChecked(false);
				alertSMS.setChecked(false);
				adapterRecipent.setType(TypeDatas.PHONE);
			}
			alertMail.setEnabled(!isChecked);
			alertSMS.setEnabled(!isChecked);
			break;
		case R.id.alertSMS:
			if (alertMail.isChecked() && isChecked) {
				adapterRecipent.setType(TypeDatas.PHONE_MAIL);
			} else if (isChecked) {
				adapterRecipent.setType(TypeDatas.PHONE);
			} else if (!alertMail.isChecked() && !alertPhone.isChecked()) {
				adapterRecipent.setType(TypeDatas.PHONE);
				// On met par défaut alert sms
				alertSMS.setChecked(true);
			}
			break;
		case R.id.alertMail:
			if (alertSMS.isChecked() && isChecked) {
				adapterRecipent.setType(TypeDatas.PHONE_MAIL);
			} else if (isChecked) {
				adapterRecipent.setType(TypeDatas.MAIL);
			} else {
				adapterRecipent.setType(TypeDatas.PHONE);
			}
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
