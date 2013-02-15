package com.binomed.dont.forget.mom.reciever;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Intent mServiceIntent = new Intent();
			mServiceIntent.setAction("<package name>.BootService");
			ComponentName service = context.startService(mServiceIntent);
			if (null == service) {
				// something really wrong here
			}
		}
	}
}
