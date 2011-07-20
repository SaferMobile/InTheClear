/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */
package org.safermobile.clear.micro.data;

//#ifndef RIM
/*
//#endif
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.SIMCardInfo;
//#ifndef RIM
*/
//#endif
 
public class PhoneInfo {

		/**
		 * get the cell id in the phone
		 * 
		 * @return
		 */
		public static String getCellId(){
			String out = "";
			try{
	 
				out = System.getProperty("Cell-ID");
				if(out== null || out.equals("null") || out.equals(""))
					out = System.getProperty("CellID");
				if(out== null ||out.equals("null")|| out.equals(""))
					System.getProperty("phone.cid");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.mid.cellid");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.sonyericsson.net.cellid");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("phone.cid");//System.getProperty("CellID");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.samsung.cellid");				
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.siemens.cellid");				
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("cid");
				
				//#ifndef RIM
				/*
				//#endif
				//
				if(out== null ||out.equals("null")|| out.equals(""))
					out = Integer.toString(GPRSInfo.getCellInfo().getCellId());
				//#ifndef RIM
				*/
				//#endif
					 
	 
			}catch(Exception e){
				return out==null?"":out;
			}
	 
			return out==null?"":out;
		}
	 
		/**
		 * get the lac sring from phone
		 */
		public static String getLAC(){
			String out = "";
			try{
	 
				out = System.getProperty("phone.lac");
	 
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.mid.lac");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.sonyericsson.net.lac");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("LocAreaCode");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.samsung.cellid");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.siemens.cellid");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("cid");
				
				//#ifndef RIM
				/*
				//#endif
				if(out== null ||out.equals("null")|| out.equals(""))
					out = Integer.toString(GPRSInfo.getCellInfo().getLAC());
				//#ifndef RIM
				*/
				//#endif
					 
	 
			}catch(Exception e){
				return out==null?"":out;
			}
	 
			return out==null?"":out;
		}
	 
		/**
		 *  Example IMSI (O2 UK): 234103530089555
			String mcc = imsi.substring(0,3); // 234 (UK)
			String mnc = imsi.substring(3,5); // 10 (O2)
		 * @return
		 */
		public static String getIMSI(){
			String out = "";
			try{
	 
				out = System.getProperty("IMSI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("phone.imsi") ;
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.mid.mobinfo.IMSI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.mid.imsi");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.sonyericsson.imsi");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("IMSI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.samsung.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.siemens.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("imsi");
				
				//#ifndef RIM
				/*
				//#endif
				if(out== null ||out.equals("null")|| out.equals(""))
					out = GPRSInfo.imeisvToString(SIMCardInfo.getIMSI(), false);
				//#ifndef RIM
				*/
				//#endif
	 
			}catch(Exception e){
				return out==null?"":out;
			}
	 
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
			try{
	 
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("phone.mcc") ;
				if(out== null ||out.equals("null")|| out.equals(""))
					//out = System.getProperty("com.nokia.mid.mobinfo.IMSI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.sonyericsson.net.mcc");
				if(out== null ||out.equals("null")|| out.equals("")){
					out = getIMSI().equals("")?"": getIMSI().substring(0,3);
				}
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.samsung.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.siemens.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("mcc");				

				//#ifndef RIM
				/*
				//#endif
				if(out== null ||out.equals("null")|| out.equals(""))
					out = Integer.toString(GPRSInfo.getCellInfo().getMCC());
				//#ifndef RIM
				*/
				//#endif
	 
			}catch(Exception e){
				return out==null?"":out;
			}
	 
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
			try{
	 
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("phone.mnc") ;
				if(out== null ||out.equals("null")|| out.equals(""))
					out = getIMSI().equals("")?"": getIMSI().substring(3,5);
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.sonyericsson.net.mnc");
				if(out== null ||out.equals("null")|| out.equals("")){
					out = getIMSI().equals("")?"": getIMSI().substring(3,5);
				}
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.samsung.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.siemens.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("mnc");
				
				//#ifndef RIM
				/*
				//#endif
				if(out== null ||out.equals("null")|| out.equals(""))
					out = Integer.toString(GPRSInfo.getCellInfo().getMNC());  
				//#ifndef RIM
				*/
				//#endif
	 
			}catch(Exception e){
				return out==null?"":out;
			}
	 
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
			try{
	 
				out = System.getProperty("com.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("phone.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.IMEI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.mid.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.nokia.mid.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.sonyericsson.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("IMEI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.motorola.IMEI");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.samsung.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("com.siemens.imei");
				if(out== null ||out.equals("null")|| out.equals(""))
					out = System.getProperty("imei");
				
				//#ifndef RIM
				/*
				//#endif
				if(out== null ||out.equals("null")|| out.equals(""))
					out =  GPRSInfo.imeiToString(GPRSInfo.getIMEI());
				// #ifndef RIM
				*/
				//#endif
	 
			}catch(Exception e){
				return out==null?"":out;
			}
	 
			return out==null?"":out;
		}
}
