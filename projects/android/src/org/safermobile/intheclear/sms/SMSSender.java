package org.safermobile.intheclear.sms;

import java.util.ArrayList;

import org.safermobile.intheclear.R;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSSender implements SMSTesterConstants {
	PendingIntent _sentPI;
	PendingIntent _deliveredPI;
	 
	Context c;
	SmsManager sms;
	
	public SMSSender(Context c) {
		this.c = c;
	}
	
	public boolean sendSMS(String recipient, String messageData) {
		_sentPI = PendingIntent.getBroadcast(c, 0, new Intent(SENT), 0);
		_deliveredPI = PendingIntent.getBroadcast(c, 0, new Intent(DELIVERED), 0);
		sms = SmsManager.getDefault();
		boolean result = false;
		ArrayList<String> splitMsg = sms.divideMessage(messageData);
		for(String msg : splitMsg) {
			try {
				sms.sendTextMessage(recipient, null, msg, _sentPI, _deliveredPI);
				result = true;
			} catch(IllegalArgumentException e) {
				result = false;
				Toast.makeText(c, c.getResources().getString(R.string.WIZARD_FAIL_SMSTEST_INVALIDNUMBER), Toast.LENGTH_LONG).show();
			} catch(NullPointerException e) {
				result = false;
				Toast.makeText(c, c.getResources().getString(R.string.WIZARD_FAIL_SMSTEST_INVALIDNUMBER), Toast.LENGTH_LONG).show();
			}
		}
		return result;
 	}
	
}