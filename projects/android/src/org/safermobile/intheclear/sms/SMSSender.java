package org.safermobile.intheclear.sms;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.telephony.SmsManager;

public class SMSSender implements SMSTesterConstants {
	PendingIntent _sentPI;
	PendingIntent _deliveredPI;
	 
	Context c;
	SmsManager sms;
	
	public SMSSender(Context c) {
		this.c = c;
	}
	
	public void sendSMS(String recipient, String messageData) {
		_sentPI = PendingIntent.getBroadcast(c, 0, new Intent(SENT), 0);
		_deliveredPI = PendingIntent.getBroadcast(c, 0, new Intent(DELIVERED), 0);
		sms = SmsManager.getDefault();
		
		ArrayList<String> splitMsg = sms.divideMessage(messageData);
		for(String msg : splitMsg) {
			sms.sendTextMessage(recipient, null, msg, _sentPI, _deliveredPI);
		}
 	}
	
}