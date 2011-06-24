package org.safermobile.utils;
/* Copyright (c) 2009, Nathan Freitas, Orbot / The Guardian Project - http://openideals.com/guardian */
/* See LICENSE for licensing information */



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

import android.content.Intent;
import android.util.Log;

public class ShellUtils {
	
	public final static String TAG = "ShellUtils";
	
	//various console cmds
	public final static String SHELL_CMD_CHMOD = "chmod";
	public final static String SHELL_CMD_KILL = "kill -9";
	public final static String SHELL_CMD_RM = "rm";
	public final static String SHELL_CMD_PS = "ps";
	public final static String SHELL_CMD_PIDOF = "pidof";

	public final static String CHMOD_EXE_VALUE = "777";
	
	public final static int DEFAULT_WAIT_FOR = 3000;
	
	private boolean runAsRoot = false;
	
	Process proc = null;
	StreamThread it;
	StreamThread et;
	StreamThread.StreamUpdate logUpdate = null;
	OutputStream stdout;
	
	public ShellUtils (boolean mRunAsRoot, StreamThread.StreamUpdate mLogUpdate) throws IOException
	{
		runAsRoot = mRunAsRoot;
		
		if (mLogUpdate != null)
			logUpdate = mLogUpdate;
		else
			logUpdate = new LogUpdate();
		

		initShell();
	
	}
	
	private void initShell() throws IOException
	{
		
		if (proc != null)
			proc.destroy();
		
		if (runAsRoot)
			proc = Runtime.getRuntime().exec("su - sh");
		else
			proc = Runtime.getRuntime().exec("sh");

		stdout = proc.getOutputStream();
		
		it = new StreamThread(proc.getInputStream(), logUpdate);
		et = new StreamThread(proc.getErrorStream(), logUpdate);

		it.start();
		et.start();

		
	}
	
	/**
	 * Check if we have root access
	 * @return boolean true if we have root
	 */
	public boolean checkRootAccess() {
	

		
		try {
			
			// Run an empty script just to check root access
			String[] cmd = {"exit 0"};
			doShellCommand(cmd);
			int exitCode = doExit();
			if (exitCode == 0) {
				
				return true;
			}
			
		} catch (IOException e) {
			//this means that there is no root to be had (normally) so we won't log anything
			logException("Error checking for root access",e);
			
		}
		catch (Exception e) {
			logException("Error checking for root access",e);
			//this means that there is no root to be had (normally)
		}
		
		logMessage("Could not acquire root permissions");
		return false;
	}
	
	public int findProcessId(String command) 
	{
		int procId = -1;
		
		try
		{
			procId = findProcessIdWithPidOf(command);
			
			if (procId == -1)
				procId = findProcessIdWithPS(command);
		}
		catch (Exception e)
		{
			try
			{
				procId = findProcessIdWithPS(command);
			}
			catch (Exception e2)
			{
				logException("Unable to get proc id for: " + command,e2);
			}
		}
		
		return procId;
	}
	
	//use 'pidof' command
	public int findProcessIdWithPidOf(String command) throws Exception
	{
		
		int procId = -1;
		
		Runtime r = Runtime.getRuntime();
		    	
		Process procPs = null;
		
		String baseName = new File(command).getName();
		//fix contributed my mikos on 2010.12.10
		procPs = r.exec(new String[] {SHELL_CMD_PIDOF, baseName});
        //procPs = r.exec(SHELL_CMD_PIDOF);
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(procPs.getInputStream()));
        String line = null;

        while ((line = reader.readLine())!=null)
        {
        
        	try
        	{
        		//this line should just be the process id
        		procId = Integer.parseInt(line.trim());
        		break;
        	}
        	catch (NumberFormatException e)
        	{
        		logException("unable to parse process pid: " + line,e);
        	}
        }
            
       
        return procId;

	}
	
	//use 'ps' command
	public int findProcessIdWithPS(String command) throws Exception
	{
		
		int procId = -1;
		
		Runtime r = Runtime.getRuntime();
		    	
		Process procPs = null;
		
        procPs = r.exec(SHELL_CMD_PS);
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(procPs.getInputStream()));
        String line = null;
        
        while ((line = reader.readLine())!=null)
        {
        	if (line.indexOf(' ' + command)!=-1)
        	{
        		
        		StringTokenizer st = new StringTokenizer(line," ");
        		st.nextToken(); //proc owner
        		
        		procId = Integer.parseInt(st.nextToken().trim());
        		
        		break;
        	}
        }
        
       
        
        return procId;

	}
	
	public int doExit () throws IOException, InterruptedException
	{
		 logMessage("> exit");
	     stdout.write(("exit" + "\n").getBytes("ASCII"));
	     logMessage("> exit");
	     stdout.write(("exit" + "\n").getBytes("ASCII"));

	     
	     return proc.waitFor();
	}

	public void doShellCommand(String[] cmds) throws Exception
	{
		doShellCommand(cmds, DEFAULT_WAIT_FOR);
	}
	
	public void doShellCommand(String[] cmds, int waitTime) throws Exception
	{
			 	
        for (int i = 0; i < cmds.length; i++)
        {
        	logMessage("> " + cmds[i]);
        	stdout.write((cmds[i] + "\n").getBytes("ASCII"));
        }
        	
       try { Thread.sleep (waitTime); }
       catch (Exception e){}
	    
	}
	
	
	public class LogUpdate implements StreamThread.StreamUpdate
	{
		StringBuilder log = new StringBuilder();
		
		@Override
		public void update(String val)
		{
			log.append(val);
			log.append('\n');
			logMessage(val);
		}
		
		public String getLog ()
		{
			return log.toString();
		}
	}

	public static void logException (String exc, Exception e)
	{
		Log.e(TAG, exc, e);
	}
	
	public static void logMessage (String msg)
	{
		Log.i(TAG, msg);
	}
	
	
}
