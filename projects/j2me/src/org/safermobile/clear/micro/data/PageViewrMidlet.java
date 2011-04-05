package org.safermobile.clear.micro.data;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class PageViewrMidlet extends MIDlet implements CommandListener {

	private Display display;
	private Splash  splash;
	private Command exit;
	
	public PageViewrMidlet() {
		this.display = Display.getDisplay(this);
		this.splash = new Splash("/splash_240x320.png", 0xFFFFFF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.exit = new Command("Exit", Command.EXIT, 0x01);
		
		Displayable main = this.getMainScreen();
		main.setCommandListener(this);
		main.addCommand(this.exit);
		
		this.splash.show(this.display, main, 3000);
	}

	/**
	 * Gets the application main screen.
	 * 
	 * @return main screen.
	 */
	private Displayable getMainScreen() {
		//TODO: Implement the main screen here.
		return new Canvas() {
			protected void paint(Graphics g) {
			}
		};
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == this.exit) {
			try {
				this.destroyApp(true);
			} catch (MIDletStateChangeException e) {
				e.printStackTrace();
			} finally {				
				this.notifyDestroyed();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}
}
