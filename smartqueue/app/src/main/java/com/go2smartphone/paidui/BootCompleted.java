package com.go2smartphone.paidui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smartpos.login.LoginActivity;

// here is the OnRevieve methode which will be called when boot completed
public class BootCompleted extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// we double check here for only boot complete event
		if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
			// here we start the service
			// Intent serviceIntent = new Intent(context,
			// AndroidStartServiceOnBoot.class);
			// context.startService(serviceIntent);
			Intent i = new Intent(context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(i);
		}
	}
}