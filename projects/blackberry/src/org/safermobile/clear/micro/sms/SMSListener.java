/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.sms;
import javax.wireless.messaging.Message;


public interface SMSListener {

	/**
	 * Called when a new message is received.
	 * 
	 * @param message new message.
	 */
	public void messageReceived(Message message);

}
