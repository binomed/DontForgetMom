package com.binomed.dont.forget.mom.screen.pref;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.binomed.dont.forget.mom.R;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);

		// add the prefernces.xml layout
		addPreferencesFromResource(R.xml.dont_forget_mom_preferences);

		// get the specified preferences using the key declared in preferences.xml
		ListPreference dataPref = (ListPreference) findPreference(getString(R.string.pref_defaut_message_key));
		dataPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				setResult(RESULT_OK);
				return true;
			}
		});
	}

}
