/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.data;

import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.SIMCardException;
import net.rim.device.api.system.SIMCardInfo;
import net.rim.device.api.system.GPRSInfo.GPRSCellInfo;
 
public class PhoneInfo {
		protected static GPRSCellInfo cellInfo;
		public PhoneInfo() {
			cellInfo = GPRSInfo.getCellInfo();
		}
		
		public static String getCellId(){
			String out = "";
			out = Integer.toString(cellInfo.getCellId());
			return out==null?"":out;
		}
	 
		/**
		 * get the lac sring from phone
		 */
		public static String getLAC(){
			String out = "";
			out = Integer.toString(cellInfo.getLAC());
			return out==null?"":out;
		}
	 
		/**
		 *  Example IMSI (O2 UK): 234103530089555
			String mcc = imsi.substring(0,3); // 234 (UK)
			String mnc = imsi.substring(3,5); // 10 (O2)
		 * @return
		 */
		public static String getIMSI() throws SIMCardException{
			String out = "";
			out = GPRSInfo.imeisvToString(SIMCardInfo.getIMSI(), false);
			return out==null?"":out;
		}
	 
		/**
		 * 
		 *  For moto, Example IMSI (O2 UK): 234103530089555
			String mcc = imsi.substring(0,3); // 234 (UK)
		 * @return
		 */
		public static String getMCC(){
			String out = "";
			out = getIMEI().substring(0,3);
			return out==null?"":out;
		}
	 
		/**
		 * 
		 *  For moto, Example IMSI (O2 UK): 234103530089555
			String mnc = imsi.substring(3,5); // 10 (O2)
		 * @return
		 */
		public static String getMNC(){
			String out = "";
			out = getIMEI().substring(3,5);
			return out==null?"":out;
		}
	 
		/**
		 * not used now
		 * get the IMEI (International Mobile Equipment Identity (IMEI)) in the phone
		 * 
		 * @return
		 */
		public static String getIMEI(){
			String out = "";
			out =  GPRSInfo.imeiToString(GPRSInfo.getIMEI());
			return out==null?"":out;
		}
}
