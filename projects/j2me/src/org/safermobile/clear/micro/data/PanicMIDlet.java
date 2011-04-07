package org.safermobile.clear.micro.data;
import java.io.IOException;
import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import org.safermobile.clear.micro.sms.SMSServer;

public class PanicMIDlet extends MIDlet implements CommandListener {

	private DisplayManager manager;
	private Displayable screen1;
	private Displayable screen2;
	private Displayable screen3;
	private Command	 back;
	private Command	 next;
	
	private SMSServer smsServer;
	private int SMS_SERVER_PORT = 0;
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicMIDlet() {
		this.manager = new DisplayManager(Display.getDisplay(this));
		this.back = new Command("Back", Command.BACK, 1);
		this.next = new Command("Next", Command.OK, 1);
		
		this.screen1 = getSreen1();
		this.screen1.setCommandListener(this);
		this.screen1.addCommand(this.back);
		this.screen1.addCommand(this.next);
		
		this.screen2 = getSreen2();
		this.screen2.setCommandListener(this);
		this.screen2.addCommand(this.back);
		this.screen2.addCommand(this.next);
		
		this.screen3 = getSreen3();
		this.screen3.setCommandListener(this);
		this.screen3.addCommand(this.back);
		this.screen3.addCommand(this.next);
	}

	private Displayable getSreen1() {
		return new List("List [Screen 1]", List.IMPLICIT);
	}

	private Displayable getSreen2() {
		return new Form("Form [Screen 2]");
	}

	private Displayable getSreen3() {
		return new TextBox("Text [Screen 3]", "", 100, TextField.ANY);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.manager.next(this.screen1);
		
		smsServer = new SMSServer (SMS_SERVER_PORT);
		try {
			smsServer.start();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		
		smsServer.sendSMSAlert("+17185697272", "what up: " + new Date().getTime());
		
		if (command == this.next) {
			if (displayable == this.screen1) {
				this.manager.next(this.screen2);
			} else
			if (displayable == this.screen2) {
				this.manager.next(this.screen3);
			}
		}
		
		if (command == this.back) {
			this.manager.back();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}
}
