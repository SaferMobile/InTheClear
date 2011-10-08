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
	
	public static String testData() {
		StringBuffer sb = new StringBuffer();
		if(getIMEI() != null)
			sb.append("IMEI: " + PhoneInfo.getIMEI() + "\n");
		if(getIMSI() != null)
			sb.append("IMSI: " + PhoneInfo.getIMSI() + "\n");
		if(getCellId() != null)
			sb.append("CID: " + PhoneInfo.getCellId() + "\n");
		if(getLAC() != null)
			sb.append("LAC: " + PhoneInfo.getLAC() + "\n");
		if(getMCC() != null)
			sb.append("MCC: " + PhoneInfo.getMCC() + "\n");
		if(getMNC() != null)
			sb.append("MNC: " + PhoneInfo.getMNC() + "\n");
		return sb.toString();
	}
	
	public static String getMyPhoneNumber() {
		String out = "";
		try {
			out = tm.getLine1Number();
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
	
	public static String getOperator() {
		String out = "";
		try {
			if(tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE)
				out = tm.getNetworkOperator();
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
	
	public static String getCellId() {	
		String out = "";
		try {
			if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
				final GsmCellLocation gLoc = (GsmCellLocation) tm.getCellLocation();
				if(gLoc != null)
					out = Integer.toString(gLoc.getCid());
			}
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
	
	public static String getLAC() {
		String out = "";
		try {
			if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM){
				final GsmCellLocation gLoc = (GsmCellLocation) tm.getCellLocation();
				if(gLoc != null)
					out = Integer.toString(gLoc.getLac());
			}
			return out;		
		} catch(NullPointerException e) {
			return "";
		}
		
	}
	
	public static String getIMSI() {
		String out = "";
		try {
			out = tm.getSubscriberId();
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
	
	public static String getMCC() {
		String out = "";
		try {
			out = tm.getNetworkOperator().substring(0,3);
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
	
	public static String getMNC() {
		String out = "";
		try {
			out = tm.getNetworkOperator().substring(3);
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
	
	public static String getIMEI() {
		String out = "";
		try {
			out = tm.getDeviceId();
			return out;
		} catch(NullPointerException e) {
			return "";
		}
	}
}