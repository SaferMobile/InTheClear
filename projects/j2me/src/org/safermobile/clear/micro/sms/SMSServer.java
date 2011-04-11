/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.sms;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

public class SMSServer implements Runnable {

	private MessageConnection connection;
	private Vector  listeners;
	private boolean stop;
	private int 	port;

	private final static String PROTOCOL = "sms://";
	/**
	 * Creates a SMS Server listening on
	 * the specified port.
	 * 
	 * @param port server port.
	 */
	public SMSServer(int port) {
		this.listeners = new Vector();
		this.port = port;
	}
	
	public void sendSMSAlert (String address, String message)
	{
		try 
		  {
		    //creates a new TextMessage
		    TextMessage textMessage = (TextMessage)connection.newMessage(MessageConnection.TEXT_MESSAGE);
		    textMessage.setAddress(PROTOCOL + address);
		    textMessage.setPayloadText(message);
		    connection.send(textMessage);
		  } 
		  catch (Exception e) {
		  }
	}
	/**
	 * Starts the server.
	 * 
	 * @throws IOException - Any connection related error.
	 */
	public void start() throws IOException {
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * Stops the server.
	 */
	public void stop() {
		this.stop = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			this.connection = (MessageConnection) Connector.open(PROTOCOL + ':' + this.port);
			
			while (!stop) {
				try {
					Message message = this.connection.receive();
					
					Enumeration enumeration = this.listeners.elements();
					while (enumeration.hasMoreElements()) {
						SMSListener listener = (SMSListener) enumeration.nextElement();
						listener.messageReceived(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a listener for new messages.
	 * 
	 * @param listener target listener.
	 */
	public void addListener(SMSListener listener) {
		if (!this.listeners.contains(listener)) {			
			this.listeners.addElement(listener);
		}
	}
	
	/**
	 * Removes a listener for new messages.
	 * 
	 * @param listener target listener.
	 */
	public void removeListener(SMSListener listener) {
		this.listeners.removeElement(listener);
	}
	
}
