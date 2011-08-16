package org.safermobile.intheclear.sms;

import android.app.Activity;

public interface SMSTesterConstants {
		public final static int SMS_INITIATED = 100;
		public final static int SMS_SENDING = 300;
		public final static int SMS_DELIVERY = 400;
		
		public final static int SMS_ATTEMPTED = Activity.RESULT_OK;
		public final static int SMS_SENT = Activity.RESULT_OK;
		public final static int SMS_DELIVERED = Activity.RESULT_OK;

		public final static String SENT = "SMS_SENT";
		public final static String DELIVERED = "SMS_DELIVERED";
		
		public final static int SMS_INVALID_NUMBER = 404;
		public final static int SMS_COMPLETE = 500;


}