package org.safermobile.clear.micro.net;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class OMNIMidlet extends MIDlet implements CommandListener, DownloadListener {

	private DownloadManager manager;
	private Display display;
	private Form	form;
	private TextField item;
	private Command download;
	private Command exit;
	
	public OMNIMidlet() {
		this.display = Display.getDisplay(this);
		this.form	 = new Form("Image Downloader");
		this.form.setCommandListener(this);
		
		this.item = new TextField("Image Url:", "http://www.eclipse.org/images/egg-incubation.png", 100, TextField.ANY);
		this.form.append(item);
		
		this.download = new Command("Download", Command.SCREEN, 1);
		this.exit = new Command("Exit", Command.EXIT, 1);
		this.form.addCommand(this.download);
		this.form.addCommand(this.exit);
		
		this.manager = new DownloadManager();
		this.manager.addListener(this);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.display.setCurrent(this.form);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == this.download) {
			this.manager.download(this.item.getString());
		} else 
		if (c == this.exit) {
			this.notifyDestroyed();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}

	public void downloadCompleted(byte[] data) {
		this.form.setTitle("Download Completed!");
		this.form.append(Image.createImage(data, 0x00, data.length));
	}

	public void downloadError(Exception e) {
		e.printStackTrace();
	}

	public void downloadStatus(int percent) {
		this.form.setTitle("Download " + percent + " %...");
	}

}
