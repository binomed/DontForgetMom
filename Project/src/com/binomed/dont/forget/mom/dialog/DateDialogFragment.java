package com.binomed.dont.forget.mom.dialog;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class DateDialogFragment extends DialogFragment {

	private DatePickerDialog.OnDateSetListener listener;
	private final int resTitle;

	public DateDialogFragment() {
		this(-1);
	}

	public DateDialogFragment(int resTitle) {
		this.resTitle = resTitle;
	}

	public void setListener(DatePickerDialog.OnDateSetListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of TimePickerDialog and return it
		return new DatePickerDialog(getActivity(), listener, year, month, day);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (resTitle != -1) {
			getDialog().setTitle(resTitle);
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
