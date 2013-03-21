package com.binomed.dont.forget.mom.view;

import java.text.DateFormat;
import java.util.Date;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import roboguice.RoboGuice;
import roboguice.inject.InjectResource;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.adapter.OldTripAdapter;
import com.binomed.dont.forget.mom.db.DontForgetMomContentProvider;
import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;
import com.binomed.dont.forget.mom.screen.edit.EditionActivity;
import com.binomed.dont.forget.mom.utils.DontForgetMomCst;

public class OldTripView extends RelativeLayout implements OnClickListener, OnActionItemClickListener {

	ImageView actionOldTrip;
	TextView oldTripName;
	TextView oldTripLaunch;

	/* Resources */
	@InjectResource(R.drawable.ic_action_replay)
	Drawable drawableActionReplay;
	@InjectResource(R.drawable.ic_action_duplicate)
	Drawable drawableActionDuplicate;
	@InjectResource(R.drawable.ic_action_edit)
	Drawable drawableActionEdit;
	@InjectResource(R.drawable.ic_action_delete)
	Drawable drawableActionDelete;
	@InjectResource(R.string.old_action_replay)
	String strActionReplay;
	@InjectResource(R.string.old_action_duplicate)
	String strActionDuplicate;
	@InjectResource(R.string.old_action_edit)
	String strActionEdit;
	@InjectResource(R.string.old_action_delete)
	String strActionDelete;
	@InjectResource(R.string.old_confirm_delete)
	String strDeleteDialogTitle;
	@InjectResource(R.string.old_confirm_delete_msg)
	String strDeleteDialogMsg;
	@InjectResource(R.string.old_confirm_delete_ok)
	String strDeleteDialogBtnOk;
	@InjectResource(android.R.string.cancel)
	String strDeleteDialogBtnCancel;
	@InjectResource(R.string.trip_never_launch)
	String strNeverLaunch;

	private static final int ACTION_REPLAY = 1;
	private static final int ACTION_DUPLICATE = 2;
	private static final int ACTION_EDIT = 3;
	private static final int ACTION_DELETE = 4;

	private int tripId;
	private final DateFormat format;
	private final QuickAction quickAction;
	private final OldTripAdapter adapter;

	public OldTripView(Context context, OldTripAdapter adapter) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_old_trips, this);
		RoboGuice.getInjector(context).injectMembers(this);

		// init adapter
		this.adapter = adapter;

		// Init the time format
		format = DateFormat.getDateInstance(DateFormat.FULL);

		// Init quickActions
		ActionItem replayActionItem = new ActionItem(ACTION_REPLAY, strActionReplay, drawableActionReplay);
		ActionItem duplicateActionItem = new ActionItem(ACTION_DUPLICATE, strActionDuplicate, drawableActionDuplicate);
		ActionItem viewActionItem = new ActionItem(ACTION_EDIT, strActionEdit, drawableActionEdit);
		ActionItem deleteActionItem = new ActionItem(ACTION_DELETE, strActionDelete, drawableActionDelete);
		quickAction = new QuickAction(context);
		quickAction.addActionItem(replayActionItem);
		quickAction.addActionItem(duplicateActionItem);
		quickAction.addActionItem(viewActionItem);
		quickAction.addActionItem(deleteActionItem);
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
			oldTripLaunch.setText(R.string.trip_never_launch);
		}

	}

	@Override
	public void onClick(View view) {
		quickAction.show(view);

	}

	@Override
	public void onItemClick(QuickAction source, int pos, int actionId) {
		switch (actionId) {
		case ACTION_DUPLICATE:
			Intent duplicateIntent = new Intent(getContext(), EditionActivity.class);
			duplicateIntent.putExtra(DontForgetMomCst.INTENT_DUPLICATE, true);
			duplicateIntent.putExtra(Trip._ID, tripId);
			getContext().startActivity(duplicateIntent);
			// Toast.makeText(getContext(), "Duplicate", Toast.LENGTH_LONG);
			break;
		case ACTION_REPLAY:
			adapter.replay(tripId);

			break;
		case ACTION_EDIT:
			Intent viewIntent = new Intent(getContext(), EditionActivity.class);
			viewIntent.putExtra(DontForgetMomCst.INTENT_DUPLICATE, true);
			viewIntent.putExtra(Trip._ID, tripId);
			getContext().startActivity(viewIntent);

			break;
		case ACTION_DELETE:
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle(strDeleteDialogTitle)//
					.setMessage(strDeleteDialogMsg) //
					.setPositiveButton(strDeleteDialogBtnOk, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ContentResolver cr = getContext().getContentResolver();
							String where = Trip._ID + " = ?";
							String[] selectionArgs = new String[] { String.valueOf(tripId) };
							cr.delete(DontForgetMomContentProvider.CONTENT_URI, where, selectionArgs);
							dialog.dismiss();
							adapter.requery();

						}
					}) //
					.setNegativeButton(strDeleteDialogBtnCancel, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					})//
					.show();

			break;

		default:
			break;
		}

	}

}
