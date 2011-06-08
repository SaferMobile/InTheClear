package org.safermobile.intheclear.sms;

import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class SMSSender {
	private SMSLogger _smsLogger;
	private SMSErrorStatusReceiver _statusRev;
	private SmsManager sms = SmsManager.getDefault();
	private TelephonyManager tm;
	
	private String _fromNumber;
	private String _toNumber;
	
	boolean _useDataPort = false;
	boolean _addMetadataTracking = true;
	
	public static void sendSMS(String recipient, String messageData, boolean metadataTracking, boolean containsData) {
		
	}
	
}