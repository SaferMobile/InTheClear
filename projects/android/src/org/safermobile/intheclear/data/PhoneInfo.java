package org.safermobile.intheclear.data;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class PhoneInfo {
	static TelephonyManager tm;
	Context c;
	
	public PhoneInfo(Context c) {
		this.c = c;
		tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static String getMyPhoneNumber() {
		String out = null;
		out = tm.getLine1Number();
		return out;
	}
	
	public static String getCellId() {	
		String out = null;
		if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
			final GsmCellLocation gLoc = (GsmCellLocation) tm.getCellLocation();
			if(gLoc != null)
				out = Integer.toString(gLoc.getCid());
		} else {
			out = "non GSM phone-- what do we do?";
		}
		return out;
	}
	
	public static String getLAC() {
		String out = null;
		if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM){
			final GsmCellLocation gLoc = (GsmCellLocation) tm.getCellLocation();
			if(gLoc != null)
				out = Integer.toString(gLoc.getLac());
		} else {
			out = "non GSM phone-- what do we do?";
		}
		return out;
	}
	
	public static String getIMSI() {
		String out = null;
		out = tm.getSubscriberId();
		return out;
	}
	
	public static String getMCC() {
		String out = null;
		out = getIMSI().substring(0,3);
		return out;
	}
	
	public static String getMNC() {
		String out = null;
		out = getIMSI().substring(3,5);
		return out;
	}
	
	public static String getIMEI() {
		String out = null;
		out = tm.getDeviceId();
		return out;
	}
}
