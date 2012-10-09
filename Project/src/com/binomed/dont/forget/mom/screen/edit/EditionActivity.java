package com.binomed.dont.forget.mom.screen.edit;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.dialog.DateDialogFragment;
import com.binomed.dont.forget.mom.dialog.TimeDialogFragment;
import com.binomed.dont.forget.mom.utils.AbstractActivity;

public class EditionActivity extends AbstractActivity {

	public static final int ITEM_SAVE = 3;

	@InjectView(R.id.tripNameTxt)
	EditText tripNameTxt;
	@InjectView(R.id.placeEdit)
	EditText placeEdit;
	@InjectView(R.id.btnMap)
	ImageButton btnMap;
	@InjectView(R.id.btnContactPlace)
	ImageButton btnContactPlace;
	@InjectView(R.id.btnDate)
	Button btnDate;
	@InjectView(R.id.btnHour)
	Button btnHour;
	@InjectView(R.id.departHour)
	CheckBox departHour;
	@InjectView(R.id.seekPrecision)
	SeekBar seekPrecision;
	@InjectView(R.id.radioGroupAlert)
	RadioGroup radioGroupAlert;
	@InjectView(R.id.btContactRecipient)
	ImageButton btContactRecipient;
	@InjectView(R.id.recipients)
	EditText recipients;
	@InjectView(R.id.messageContent)
	EditText messageContent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, ITEM_SAVE, 1, R.string.action_save) //
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
		case R.id.btnMap:
			break;
		case R.id.btnContactPlace:
			break;
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
		case R.id.btContactRecipient:

			break;

		default:
			break;
		}
	}

}
