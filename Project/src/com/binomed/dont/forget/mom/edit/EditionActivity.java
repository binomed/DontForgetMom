package com.binomed.dont.forget.mom.edit;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.AbstractActivity;

public class EditionActivity extends AbstractActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

}
