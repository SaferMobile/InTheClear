package org.safermobile.intheclear.sms;

import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSSender implements SMSTesterConstants {
	PendingIntent _sentPI, _deliveredPI;
	
	public SMSConfirm smsconfirm;
	boolean result = false;
	
	Context c;
	SmsManager sms;
	
	public SMSSender(Context c) {
		this.c = c;
		this.smsconfirm = new SMSConfirm();
	}
	
	public void sendResult(String res) {
		// broadcast result back to calling activity
		
		Log.d(ITCConstants.Log.ITC,"send to: " + c.getClass() + " , result is now " + res);
	}
	
	public void sendSMS(String recipient, String messageData) {
		_sentPI = PendingIntent.getBroadcast(this.c, 0, new Intent(SENT), 0);
		_deliveredPI = PendingIntent.getBroadcast(this.c, 0, new Intent(DELIVERED), 0);
		
		c.registerReceiver(smsconfirm, new IntentFilter(SENT));
		c.registerReceiver(smsconfirm, new IntentFilter(DELIVERED));
		
		sms = SmsManager.getDefault();
		
		ArrayList<String> splitMsg = sms.divideMessage(messageData);
		for(String msg : splitMsg) {
			try {
				sms.sendTextMessage(recipient, null, msg, _sentPI, _deliveredPI);
			} catch(IllegalArgumentException e) {
				Toast.makeText(c, c.getResources().getString(R.string.WIZARD_FAIL_SMSTEST_INVALIDNUMBER), Toast.LENGTH_LONG).show();
			} catch(NullPointerException e) {
				Toast.makeText(c, c.getResources().getString(R.string.WIZARD_FAIL_SMSTEST_INVALIDNUMBER), Toast.LENGTH_LONG).show();
			}
		}
 	}
	
	public class SMSConfirm extends BroadcastReceiver {
		int smsAttempted, smsSent, smsDelivered = SMS_INITIATED;
		
		public SMSConfirm() {
			smsAttempted = SMS_ATTEMPTED;
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().compareTo(SENT) == 0) {
				if(getResultCode() == SMS_SENT)
					smsSent = SMS_SENT;
				else {
					smsSent = getResultCode();
					returnStatus();
				}
			} else if(intent.getAction().compareTo(DELIVERED) == 0) {
				if(getResultCode() == SMS_DELIVERED) {
					smsDelivered = SMS_DELIVERED;
					returnStatus();
				} else {
					smsDelivered = getResultCode();
					returnStatus();
				}
			}
			
		}
		
		public void returnStatus() {
			SMSSender.this.sendResult("attempted: " + smsAttempted + " | sent: " + smsSent + " | delivered: " + smsDelivered);
		}
		
	}
	
}