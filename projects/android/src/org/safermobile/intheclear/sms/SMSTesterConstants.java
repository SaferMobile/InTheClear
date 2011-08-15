package org.safermobile.intheclear.sms;

import android.app.Activity;

public interface SMSTesterConstants {
		public final static int SMS_INITIATED = 100;
		public final static int SMS_ATTEMPTED = 200;
		
		public final static int SMS_SENT = Activity.RESULT_OK;
		public final static int SMS_DELIVERED = Activity.RESULT_OK;

		public final static String SENT = "SMS_SENT";
		public final static String DELIVERED = "SMS_DELIVERED";


}