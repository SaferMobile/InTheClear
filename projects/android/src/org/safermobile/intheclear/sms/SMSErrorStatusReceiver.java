package org.safermobile.intheclear.sms;

import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSErrorStatusReceiver extends BroadcastReceiver {

		
		private String _fromPhoneNumber;
		private String _toPhoneNumber;
		
		private SMSLogger _smsLogger;
		
		public SMSErrorStatusReceiver (String fromPhoneNumber, String toPhoneNumber, SMSLogger smsLogger)
		{
			_fromPhoneNumber = fromPhoneNumber;
			_toPhoneNumber = toPhoneNumber;
			
			_smsLogger = smsLogger;
		}
		
		
		@Override
		 public void onReceive(Context context, Intent intent) {
			
			int resultCode = getResultCode();
			String resultTxt = "unknown error";
			
			Date ts = new Date();
			
	        switch (resultCode)
	        {
	            case Activity.RESULT_OK:
	            	resultTxt = "sent";
	                break;
	            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	            	resultTxt = "generic failure";
	                break;
	            case SmsManager.RESULT_ERROR_NO_SERVICE:
	            	resultTxt = "error no service";
	                break;
	            case SmsManager.RESULT_ERROR_NULL_PDU:
	            	resultTxt = "error null pdu";
	                break;
	            case SmsManager.RESULT_ERROR_RADIO_OFF:
	            	resultTxt = "radio off";
	                break;
	        }
	        
	        if (resultCode != Activity.RESULT_OK)
	        	_smsLogger.logError(_fromPhoneNumber, _toPhoneNumber, resultTxt, ts,  "", "", "");
	        
	    }
}
