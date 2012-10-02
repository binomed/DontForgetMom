package com.binomed.dont.forget.mom.view;

import java.text.DateFormat;
import java.util.Date;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
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

	@InjectView(R.id.actionOldTrip)
	ImageView actionOldTrip;
	@InjectView(R.id.oldTripName)
	TextView oldTripName;
	@InjectView(R.id.oldTripLaunch)
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

		// Init listener
		actionOldTrip.setOnClickListener(this);
	}

	public void setTrip(String tripName, Date launch) {
		oldTripName.setText(tripName);
		oldTripLaunch.setText(format.format(launch));

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
