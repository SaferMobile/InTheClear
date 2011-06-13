package org.safermobile.clear.micro.apps.controllers;

import java.util.Date;

import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.data.PhoneInfo;
import org.safermobile.clear.micro.sms.SMSManager;
import org.safermobile.micro.utils.Preferences;
import org.safermobile.micro.utils.StringTokenizer;

public class ShoutController {

	
	/*
	 * localized resources
	 */
	L10nResources l10n = L10nResources.getL10nResources("en-US");
	
	public String buildShoutMessage (String userName, String userMessage, String userLocation)
	{
		
		
		StringBuffer sbPanicMsg = new StringBuffer();
		
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_FROM));
		sbPanicMsg.append(' ');
		sbPanicMsg.append(userName);
		sbPanicMsg.append(':');
		sbPanicMsg.append(' ');
		sbPanicMsg.append(userMessage);

		sbPanicMsg.append(' ');
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LOCATION));
		sbPanicMsg.append(userLocation);
		
		
		//append timestamp
		sbPanicMsg.append(' ');
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_TIMESTAMP));
		sbPanicMsg.append(new Date().toString());
	
		return sbPanicMsg.toString();
	}
	
	public String buildShoutData (String userName)
	{
		
		
		StringBuffer sbPanicMsg = new StringBuffer();
		
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_FROM));
		sbPanicMsg.append(' ');
		sbPanicMsg.append(userName);
		sbPanicMsg.append(':');
		
		
		String IMEI = PhoneInfo.getIMEI();
		if (IMEI != null && IMEI.length() > 0)
		{

			sbPanicMsg.append(" ");
			sbPanicMsg.append("IMEI:");
			sbPanicMsg.append(IMEI);

		}
		
		String IMSI = PhoneInfo.getIMSI();
		if (IMSI != null && IMSI.length() > 0)
		{
			sbPanicMsg.append(" ");
			sbPanicMsg.append("IMSI:");
			sbPanicMsg.append(IMSI);
		}
		
		//append loc info
		String cid = PhoneInfo.getCellId();
		if (cid != null && cid.length() > 0)
		{
			sbPanicMsg.append(" ");
			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_CID));
			sbPanicMsg.append(cid);
		}
		
		
		String lac = PhoneInfo.getLAC();
		if (lac != null && lac.length() > 0)
		{
			sbPanicMsg.append(" ");
			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LAC));
			sbPanicMsg.append(lac);
		}
		
		
		/*
		if (_useGPS)
		{
			// #if hasLocationCapability
			
			if (_lastLocation != null)
			{	
				sbPanicMsg.append(" GPS:");
				sbPanicMsg.append(_lastLocation.getQualifiedCoordinates().getLatitude());
				sbPanicMsg.append(",");
				sbPanicMsg.append(_lastLocation.getQualifiedCoordinates().getLongitude());

				sbPanicMsg.append(' ');
			}
			// #endif
		}*/
		
		//append timestamp
		sbPanicMsg.append(" ");
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_TIMESTAMP));
		sbPanicMsg.append(new Date().toString());
	
		return sbPanicMsg.toString();
	}
	
	public void sendAutoSMSShout (Preferences prefs) throws Exception
	{
		String recipients = prefs.get(PanicConstants.PREFS_KEY_RECIPIENT);
		String userName =  prefs.get(PanicConstants.PREFS_KEY_NAME);
		String userMessage = prefs.get(PanicConstants.PREFS_KEY_MESSAGE);
		String userLocation = prefs.get(PanicConstants.PREFS_KEY_LOCATION);
		
		String shoutMsg = buildShoutMessage(userName, userMessage, userLocation);
		String shoutData = buildShoutData (userName);
		
		
		sendSMSShout (recipients, shoutMsg, shoutData);
		
	}
	
	public void sendSMSShout (String recipients, String panicMsg, String panicData) throws Exception
	{
		
		StringTokenizer st = new StringTokenizer(recipients,",");
		
		while (st.hasMoreTokens())
		{
			String recp = st.nextToken().trim();
						
			SMSManager.sendSMSAlert(recp, panicMsg);		
			
			if (panicData == null)
				SMSManager.sendSMSAlert(recp, panicData);
		
		}
		
	}
}
