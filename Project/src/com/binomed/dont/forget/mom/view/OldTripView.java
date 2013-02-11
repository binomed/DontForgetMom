package com.binomed.dont.forget.mom.view;

import java.text.DateFormat;
import java.util.Date;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binomed.dont.forget.mom.R;

public class OldTripView extends RelativeLayout implements OnClickListener, OnActionItemClickListener {

	ImageView actionOldTrip;
	TextView oldTripName;
	TextView oldTripLaunch;

	/* Resources */
	@InjectResource(R.drawable.ic_action_replay)
	Drawable drawableActionReplay;
	@InjectResource(R.drawable.ic_action_duplicate)
	Drawable drawableActionDuplicate;
	@InjectResource(R.drawable.ic_action_about)
	Drawable drawableActionView;
	@InjectResource(R.string.old_action_replay)
	String strActionReplay;
	@InjectResource(R.string.old_action_duplicate)
	String strActionDuplicate;
	@InjectResource(R.string.old_action_view)
	String strActionView;

	private static final int ACTION_REPLAY = 1;
	private static final int ACTION_DUPLICATE = 2;
	private static final int ACTION_VIEW = 3;

	private int tripId;
	private final DateFormat format;
	private final QuickAction quickAction;

	public OldTripView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_old_trips, this);
		RoboGuice.getInjector(context).injectMembers(this);

		// Init the time format
		format = DateFormat.getDateInstance(DateFormat.FULL);

		// Init quickActions
		ActionItem replayActionItem = new ActionItem(ACTION_REPLAY, strActionReplay, drawableActionReplay);
		ActionItem duplicateActionItem = new ActionItem(ACTION_DUPLICATE, strActionDuplicate, drawableActionDuplicate);
		ActionItem viewActionItem = new ActionItem(ACTION_VIEW, strActionView, drawableActionView);
		quickAction = new QuickAction(context);
		quickAction.addActionItem(replayActionItem);
		quickAction.addActionItem(duplicateActionItem);
		quickAction.addActionItem(viewActionItem);
		quickAction.setOnActionItemClickListener(this);

		// @InjectView(R.id.actionOldTrip)
		actionOldTrip = (ImageView) findViewById(R.id.actionOldTrip);
		// @InjectView(R.id.oldTripName)
		oldTripName = (TextView) findViewById(R.id.oldTripName);
		// @InjectView(R.id.oldTripLaunch)
		oldTripLaunch = (TextView) findViewById(R.id.oldTripLaunch);

		// Init listener
		actionOldTrip.setOnClickListener(this);
	}

	public void setTrip(int tripId, String tripName, Date launch) {
		this.tripId = tripId;
		oldTripName.setText(tripName);
		if (launch != null) {
			oldTripLaunch.setText(format.format(launch));
		} else {
			// TODO gerer le cas des nons lancements
		}

	}

	@Override
	public void onClick(View view) {
		quickAction.show(view);

	}

	@Override
	public void onItemClick(QuickAction source, int pos, int actionId) {
		// TODO Auto-generated method stub

	}

}
