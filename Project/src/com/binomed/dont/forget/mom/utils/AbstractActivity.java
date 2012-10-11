package com.binomed.dont.forget.mom.utils;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnConfigurationChangedEvent;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnStartEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentViewListener;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboContext;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.screen.HomeFragment;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class AbstractActivity extends SlidingFragmentActivity implements RoboContext {

	private static final int ITEM_ABOUT = 1;
	private static final int ITEM_INFO = 2;

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			// final Intent intent = new Intent(this, DontForgetMomActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			return true;
		case ITEM_ABOUT:
			Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
			return true;
		case ITEM_INFO:
			Toast.makeText(this, "Info", Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onMenuItemSelected(featureId, item);
		}

	}

	protected EventManager eventManager;
	protected HashMap<Key<?>, Object> scopedObjects = new HashMap<Key<?>, Object>();

	@Inject
	ContentViewListener ignored; // BUG find a better place to put this

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final RoboInjector injector = RoboGuice.getInjector(this);
		eventManager = injector.getInstance(EventManager.class);
		injector.injectMembersWithoutViews(this);
		super.onCreate(savedInstanceState);
		eventManager.fire(new OnCreateEvent(savedInstanceState));

		setBehindContentView(R.layout.view_empty_slide);
		getSupportFragmentManager().beginTransaction().add(R.id.emptySlide, new HomeFragment()).commit();
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.actionbar_home_width);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);

		setSlidingActionBarEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, ITEM_ABOUT, Menu.NONE, R.string.action_about) //
				.setIcon(R.drawable.ic_action_about) //
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		menu.add(0, ITEM_INFO, Menu.NONE, R.string.action_help) //
				.setIcon(R.drawable.ic_action_help) //
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		eventManager.fire(new OnRestartEvent());
	}

	@Override
	protected void onStart() {
		super.onStart();
		eventManager.fire(new OnStartEvent());
	}

	@Override
	protected void onResume() {
		super.onResume();
		eventManager.fire(new OnResumeEvent());
	}

	@Override
	protected void onPause() {
		super.onPause();
		eventManager.fire(new OnPauseEvent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		eventManager.fire(new OnNewIntentEvent());
	}

	@Override
	protected void onStop() {
		try {
			eventManager.fire(new OnStopEvent());
		} finally {
			super.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		try {
			eventManager.fire(new OnDestroyEvent());
		} finally {
			try {
				RoboGuice.destroyInjector(this);
			} finally {
				super.onDestroy();
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		final Configuration currentConfig = getResources().getConfiguration();
		super.onConfigurationChanged(newConfig);
		eventManager.fire(new OnConfigurationChangedEvent(currentConfig, newConfig));
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		RoboGuice.getInjector(this).injectViewMembers(this);
		eventManager.fire(new OnContentChangedEvent());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		eventManager.fire(new OnActivityResultEvent(requestCode, resultCode, data));
	}

	@Override
	public Map<Key<?>, Object> getScopedObjectMap() {
		return scopedObjects;
	}
}
