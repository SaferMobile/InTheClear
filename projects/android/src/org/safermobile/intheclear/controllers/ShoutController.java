package org.safermobile.intheclear.controllers;

import java.util.Date;
import java.util.StringTokenizer;

import org.safermobile.intheclear.R;
import org.safermobile.intheclear.data.PhoneInfo;
import org.safermobile.intheclear.sms.SMSSender;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class ShoutController {
	Resources res;
	PhoneInfo pi;
	
	public ShoutController(Context c) {
		res = c.getResources();
		pi = new PhoneInfo(c);
	}
	
	public String buildShoutMessage(String userName, String userMessage, String userLocation) {
		StringBuffer sbPanicMsg = new StringBuffer();
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_FROM) + " " + userName + ":\n" + userMessage + "\n\n");
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_LOCATION) + " " + userLocation + "\n");
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_TIMESTAMP) + " " + new Date().toString());
		return sbPanicMsg.toString();
	}
	
	public String buildShoutData(String userName) {
		StringBuffer sbPanicMsg = new StringBuffer();
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_FROM) + " " + userName + ":\n\n");
		if(PhoneInfo.getIMEI().length() > 0)
			sbPanicMsg.append("IMEI: " + PhoneInfo.getIMEI() + "\n");		
		if(PhoneInfo.getIMSI().length() > 0)
			sbPanicMsg.append("IMSI: " + PhoneInfo.getIMSI() + "\n");
		if(PhoneInfo.getCellId().length() > 0)
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_CID) + " " + PhoneInfo.getCellId() + "\n");
		if(PhoneInfo.getLAC().length() > 0)
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_LAC) + " " + PhoneInfo.getLAC() + "\n");
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_TIMESTAMP) + " " + new Date().toString());
		return sbPanicMsg.toString();
	}
	
	public void sendAutoSMSShout(SharedPreferences sp) {
		 String recipients = sp.getString("ConfiguredFriends", "");
		 String userName = sp.getString("UserDisplayName", "");
		 String userLocation = sp.getString("UserDisplayLocation", "");
		 String userMessage = sp.getString("DefaultPanicMsg", "");
		 
		 String shoutMsg = buildShoutMessage(userName,userMessage,userLocation);
		 String shoutData = buildShoutData(userName);
		 
		 sendSMSShout(recipients,shoutMsg,shoutData);
	}
	
	public void sendSMSShout(String recipients, String shoutMsg, String shoutData) {
		StringTokenizer st = new StringTokenizer(recipients,",");
		while(st.hasMoreTokens()) {
			SMSSender.sendSMS(st.nextToken().trim(), shoutMsg + "\n\n" + shoutData, true, false);
		}
	}
}