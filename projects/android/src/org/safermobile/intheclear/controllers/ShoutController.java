package org.safermobile.intheclear.controllers;

import java.util.Date;
import java.util.StringTokenizer;

import org.safermobile.intheclear.R;
import org.safermobile.intheclear.data.MovementTracker;
import org.safermobile.intheclear.data.PhoneInfo;
import org.safermobile.intheclear.sms.SMSSender;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

public class ShoutController {
	Resources res;
	PhoneInfo pi;
	SMSSender sms;
	MovementTracker mt;

	private final static String ITC = "[InTheClear:ShoutController] ************************ ";
	
	public ShoutController(Context c) {
		res = c.getResources();
		pi = new PhoneInfo(c);
		sms = new SMSSender(c);
		mt = new MovementTracker(c);
	}
	
	public String buildShoutMessage(String userMessage) {
		StringBuffer sbPanicMsg = new StringBuffer();
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_FROM)+ ":\n" + userMessage + "\n\n");
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_TIMESTAMP) + " " + new Date().toString());
		return sbPanicMsg.toString();
	}
	
	public String buildShoutData() {
		StringBuffer sbPanicMsg = new StringBuffer();
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_FROM) + ":\n\n");
		if(PhoneInfo.getIMEI().length() > 0)
			sbPanicMsg.append("IMEI: " + PhoneInfo.getIMEI() + "\n");		
		if(PhoneInfo.getIMSI().length() > 0)
			sbPanicMsg.append("IMSI: " + PhoneInfo.getIMSI() + "\n");
		if(PhoneInfo.getCellId().length() > 0)
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_CID) + " " + PhoneInfo.getCellId() + "\n");
		if(PhoneInfo.getLAC().length() > 0)
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_LAC) + " " + PhoneInfo.getLAC() + "\n");
		if(PhoneInfo.getMCC().length() > 0)
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_MCC) + " " + PhoneInfo.getMCC() + "\n");
		if(PhoneInfo.getMNC().length() > 0)
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_MNC) + " " + PhoneInfo.getMNC() + "\n");
		if(MovementTracker.updateLocation() != null) {
			double[] location = MovementTracker.updateLocation();
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_LAT) + " " + location[0] + "\n");
			sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_LNG) + " " + location[1] + "\n");
		}
		sbPanicMsg.append(res.getString(R.string.KEY_PANIC_MSG_TIMESTAMP) + " " + new Date().toString());
		return sbPanicMsg.toString();
	}
	
	public void sendAutoSMSShout(SharedPreferences sp) {
		 String recipients = sp.getString("ConfiguredFriends", "");
		 String userMessage = sp.getString("DefaultPanicMsg", "");
		 
		 String shoutMsg = buildShoutMessage(userMessage);
		 String shoutData = buildShoutData();
		 
		 sendSMSShout(recipients,shoutMsg,shoutData);
	}
	
	public void sendSMSShout(String recipients, String shoutMsg, String shoutData) {
		StringTokenizer st = new StringTokenizer(recipients,",");
		while(st.hasMoreTokens()) {
			String recipient = st.nextToken().trim();
			sms.sendSMS(recipient, shoutMsg + "\n\n(1/2)");
			sms.sendSMS(recipient, shoutData + "\n\n(2/2)");
		}
	}
}