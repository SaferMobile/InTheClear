package org.safermobile.intheclear.sms;

import java.io.File;
import java.util.Date;

import android.util.Log;

public class SMSLogger implements SMSTesterConstants {

		private String _logMode = null;	
		private String _basePath = null;
		private File _logFile = null;
		
		public final static String MODE_SEND = "send";
		public final static String MODE_RECV = "recv";
		public final static String MODE_RECV_DATA = "recvdata";
		
		public SMSLogger (String logMode, String basePath)
		{
			_logMode = logMode;
			_basePath = basePath;
			
			if (_logFile == null)
			{
				init();
			}
		}
		
		public void init ()
		{
			
			File fileDir = new File(_basePath);
			
			if (!fileDir.exists())
				fileDir.mkdir();
			
			//load existing log data
			_logFile = new File(_basePath,"smstester" + "-" + _logMode + ".csv");
		
		}
		
		public void rotateLogFile () 
		{
			
			String logData = Utils.loadTextFile(_logFile);
		
			File newLogFile = new File(_basePath, generateTimestampLogFileName());
			
			Utils.saveTextFile(newLogFile, logData, false);		
			
			Utils.saveTextFile(_logFile, "", false);
			
			
		}
		
		private String generateTimestampLogFileName ()
		{
			//copy it to new file
			Date logDate = new Date();
			return "smstester" + "-" + _logMode + "-" + logDate.getYear() + logDate.getMonth() + logDate.getDate() + "-" + logDate.getHours() + logDate.getMinutes() + logDate.getSeconds() + ".csv";
		}
		
		public File getLogFile ()
		{
			return _logFile;
		}
		
		/*
		public void logStart (String operator, String cid, String lac, Date ts)
		{
			String[] vals = {"start",operator, cid, lac,ts.getTime()+""};
			String log = generateCSV(vals) + "\n";
			Log.i(TAG, log);
			
			
			Utils.saveTextFile(_logFile, log, true);
		
		}*/
		
		
		public void logSend (String from, String to, String smsMsg, Date ts, String operator, String cid, String lac)
		{
			String[] vals = {"sent",from,to,smsMsg,ts.getTime()+"",operator,cid,lac};
			String log = generateCSV(vals) + "\n";
			Log.i(TAG, log);
			
			Utils.saveTextFile(_logFile, log, true);
		
		}
		
		public void logReceive (String mode, String from, String to, String smsMsg, Date ts, String operator, String cid, String lac)
		{
			String[] vals = {mode,from,to,smsMsg,ts.getTime()+"",operator,cid,lac};
			
			String log = generateCSV(vals) + "\n";
			
			Log.i(TAG, log);
			

			Utils.saveTextFile(_logFile, log, true);

		
		}
		
		public void logError (String from, String to, String error, Date ts, String operator, String cid, String lac)
		{
			String[] vals = {"err",from,to,error,ts.getTime()+"",operator,cid,lac};
			String log = generateCSV(vals) + "\n";
			Log.i(TAG, log);
			
			
			Utils.saveTextFile(_logFile, log, true);

		}
		
		public void logDelivery (String from, String to, String deliveryStatus, Date ts)
		{
			String[] vals = {"del",from,to,deliveryStatus,ts.getTime()+""};
			String log = generateCSV(vals) + "\n";
			Log.i(TAG, log);
			
			
			Utils.saveTextFile(_logFile, log, true);

		}
		
		private String generateCSV(String[] params)
		{
			StringBuffer csv = new StringBuffer();
			
			for (int i = 0; i < params.length; i++)
			{
				csv.append(params[i]);
				
				if ((i+1)<params.length)
					csv.append(',');
				
			}
			
			
			return csv.toString();
		}
		
}
