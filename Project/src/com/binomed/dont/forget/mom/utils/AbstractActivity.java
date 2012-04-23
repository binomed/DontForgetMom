package com.binomed.dont.forget.mom.utils;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.DontForgetMomActivity;

public abstract class AbstractActivity extends SherlockActivity {

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			final Intent intent = new Intent(this, DontForgetMomActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

}
