package org.safermobile.clear.micro.sms;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.TextMessage;

public class RemoteControlMidlet extends MIDlet implements CommandListener, SMSListener {
	
	private SMSServer server;
	private Display   display;
	private TextBox   textBox;
	private Command   exit;
	private Command   start;
	private Command   stop;
	
	public RemoteControlMidlet() {
		this.display = Display.getDisplay(this);
		this.textBox = new TextBox("Messages", "", 100, TextField.ANY);
		this.textBox.setCommandListener(this);
		
		this.exit  = new Command("Exit", Command.EXIT, 1);
		this.start = new Command("Start", Command.SCREEN, 1);
		this.stop  = new Command("Stop", Command.SCREEN, 2);
		this.textBox.addCommand(exit);
		this.textBox.addCommand(start);
		this.textBox.addCommand(stop);
	}

	protected void startApp() throws MIDletStateChangeException {
		this.display.setCurrent(this.textBox);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == this.exit) {
			if (this.server != null) {
				this.server.stop();
			}
			this.notifyDestroyed();
		} else
		if (command == this.start) {
			if (this.server != null) {
				this.server.stop();
			}
			try {				
				this.server = new SMSServer(1234);
			
				this.server.addListener(this);
				this.server.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
		if (command == this.stop) {
			if (this.server != null) {
				this.server.stop();
			}
		}
	}

	/* (non-Javadoc)
	 * @see SMSListener#messageReceived(javax.wireless.messaging.Message)
	 */
	public void messageReceived(Message message) {
		if (message instanceof TextMessage) {
			TextMessage txt = (TextMessage) message;
			this.textBox.setString(txt.getPayloadText());
			
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}

}
