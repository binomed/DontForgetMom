package com.binomed.dont.forget.mom.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.binomed.dont.forget.mom.service.alarm.BootService;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Intent mServiceIntent = new Intent(context, BootService.class);
			context.startService(mServiceIntent);

		}
	}

}
