package org.safermobile.intheclear.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSSender implements SMSTesterConstants {
	PendingIntent _sentPI, _deliveredPI;
	
	public SMSConfirm smsconfirm;
	boolean result = false;
	
	Context c;
	SmsManager sms;
	public SMSThread smsThread;
	private Handler smsHandler;
	
	public SMSSender(Context c, Handler callback) {
		this.c = c;
		this.smsHandler = callback;
		this.smsconfirm = new SMSConfirm();
	}
	
	public void sendSMS(String recipient, String messageData) {
		_sentPI = PendingIntent.getBroadcast(this.c, 0, new Intent(SENT), 0);
		_deliveredPI = PendingIntent.getBroadcast(this.c, 0, new Intent(DELIVERED), 0);
		
		sms = SmsManager.getDefault();
		
		ArrayList<String> splitMsg = sms.divideMessage(messageData);
		for(String msg : splitMsg) {
			try {
				sms.sendTextMessage(recipient, null, msg, _sentPI, _deliveredPI);
			} catch(IllegalArgumentException e) {
				smsThread.exitWithResult(false, SMS_INITIATED, SMS_INVALID_NUMBER);
			} catch(NullPointerException e) {
				smsThread.exitWithResult(false, SMS_INITIATED, SMS_INVALID_NUMBER);
			}
		}
 	}
	
	public class SMSThread extends Thread {
				
		@Override
		public void run() {			
			c.registerReceiver(smsconfirm, new IntentFilter(SENT));
			c.registerReceiver(smsconfirm, new IntentFilter(DELIVERED));			
		}
		
		public void exitWithResult(boolean result, int process, int status) {
			Message smsStatus = new Message();
			Map<String,Integer> msg = new HashMap<String,Integer>();
			int r = 1;
			if(result != false)
				r = -1;
			
			msg.put("smsResult", r);
			msg.put("process", process);
			msg.put("status", status);
			
			smsStatus.obj = msg;
			smsHandler.sendMessage(smsStatus);
		}
	}
	
	public class SMSConfirm extends BroadcastReceiver {
		public SMSConfirm() {
			smsThread = new SMSThread();
			smsThread.start();
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().compareTo(SENT) == 0) {
				if(getResultCode() != SMS_SENT) {
					// the attempt to send has failed.
					smsThread.exitWithResult(false, SMS_SENDING, getResultCode());
					context.unregisterReceiver(this);
				}
			} else if(intent.getAction().compareTo(DELIVERED) == 0) {
				if(getResultCode() != SMS_DELIVERED) {
					// the attempt to deliver has failed.
					smsThread.exitWithResult(false, SMS_DELIVERY, getResultCode());
					context.unregisterReceiver(this);
				} else {
					smsThread.exitWithResult(true, SMS_DELIVERY, getResultCode());
					context.unregisterReceiver(this);
					
				}
			}
			
		}
		
		
		
	}
	
}