/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.sms;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

import org.safermobile.micro.utils.Logger;

public class SMSManager 
{

	private MessageConnection connSrv;

	
	//private static Hashtable _instances;
	private final static String PROTOCOL = "sms://";
	
	/**
	 * Creates a SMS Server listening on
	 * the specified port.
	 * 
	 * @param port server port.
	 */
	public SMSManager() {
	}
	
	
	public static void sendSMSAlert (String phoneNumber, String message) throws InterruptedIOException, IOException
	{
		
		    String url = PROTOCOL +  phoneNumber;
		    MessageConnection connection =
		    	(MessageConnection) Connector.open(url);
		    	TextMessage msg = (TextMessage) connection.newMessage(
		    	MessageConnection.TEXT_MESSAGE);
		    	msg.setPayloadText(message);
		    	connection.send(msg);
		    	connection.close();
		 
	}
	
	/**
	 * Starts the server.
	 * 
	 * @throws IOException - Any connection related error.
	 */
	public void start(int port) throws IOException {
		
		if (connSrv != null)
			stop();
		
		connSrv = (MessageConnection) Connector.open(PROTOCOL + ':' + port);
		
	}
	
	/**
	 * Adds a listener for new messages.
	 * 
	 * @param listener target listener.
	 * @throws IOException 
	 */
	public void setListener(MessageListener listener) throws IOException {
		connSrv.setMessageListener(listener);
	}
	
	
	public void stop () throws IOException
	{
		 if (connSrv != null) {
			 connSrv.setMessageListener(null);
			 connSrv.close();
		    }
	}
}
