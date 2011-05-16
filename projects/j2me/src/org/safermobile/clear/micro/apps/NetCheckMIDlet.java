/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */
package org.safermobile.clear.micro.apps;

import java.io.IOException;
import java.util.Date;

import javax.microedition.io.HttpsConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pki.Certificate;

import org.safermobile.clear.micro.net.DownloadListener;
import org.safermobile.clear.micro.net.DownloadManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;

// release.build = false
public class NetCheckMIDlet extends MIDlet implements CommandListener, DownloadListener {

	private DownloadManager manager;
	private Display display;
	private Form	form;
	private TextField item;
	private Command download;
	private Command exit;
	
	
	private String lastUrl;
	L10nResources l10n = L10nResources.getL10nResources("en-US");
	
	private final static String TAG = "NetCheck";
	
	public NetCheckMIDlet() {
		this.display = Display.getDisplay(this);
		
		initForm("https://");

		this.manager = new DownloadManager();
		this.manager.addListener(this);
	}
	
	private void initForm (String url)
	{
		this.form	 = new Form("NetCheck");
		this.form.setCommandListener(this);
		
		this.form.append(new StringItem(null,l10n.getString(L10nConstants.keys.KEY_NC_INTRO)));
		this.item = new TextField(l10n.getString(L10nConstants.keys.KEY_NC_ENTER), url, 200, TextField.URL);
		this.form.append(item);
		
		this.download = new Command(l10n.getString(L10nConstants.keys.KEY_START), Command.SCREEN, 1);
		this.exit = new Command(l10n.getString(L10nConstants.keys.KEY_EXIT), Command.EXIT, 1);
		this.form.addCommand(this.download);
		this.form.addCommand(this.exit);
		
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
			
			lastUrl = this.item.getString();
			
			if (lastUrl.toLowerCase().startsWith("https"))
			{

				initForm(lastUrl);
				this.display.setCurrent(this.form);
				this.manager.download(lastUrl);
			}
			else
			{
				this.showAlert(l10n.getString(L10nConstants.keys.KEY_WARNING), l10n.getString(L10nConstants.keys.KEY_NC_ERROR), form);
			}
		} else 
		if (c == this.exit) {
			this.notifyDestroyed();
		}
	}
	

	public void showAlert (String title, String msg, Displayable next)
	{
		/*
		Alert alert = new Alert(title);
		alert.setString(msg);
		alert.setTimeout(5000);
		display.setCurrent(alert, form);
		*/
		form.append(new StringItem(title,msg));

        
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
		

		HttpsConnection hConn = manager.getConnection();
		
		if (data.length > 0 && hConn != null)
		{

			this.form.setTitle(l10n.getString(L10nConstants.keys.KEY_NC_COMPLETE));
			
			try
			{
				
				String cipherSuite = hConn.getSecurityInfo().getCipherSuite();
				form.append(new StringItem("Cipher Suite",cipherSuite));
				
				String protocol = hConn.getSecurityInfo().getProtocolName();
				String protocolVersion = hConn.getSecurityInfo().getProtocolVersion();
	
				form.append(new StringItem("Protocol",protocol + " v" + protocolVersion));
				
				Certificate cert = hConn.getSecurityInfo().getServerCertificate();
				
				String issuer = cert.getIssuer();
				form.append(new StringItem("Cert Issuer",issuer));
				
				String serNum = cert.getSerialNumber();
				form.append(new StringItem("Cert Ser#",serNum));
	
				String subject = cert.getSubject();
				form.append(new StringItem("Cert Subject",subject));
	
				String type = cert.getType();
				form.append(new StringItem("Cert Type",type));
	
				String version = cert.getVersion();
				form.append(new StringItem("Cert Version",version));
	
				Date dateIssued = new Date(cert.getNotBefore());
				form.append(new StringItem("Cert Issued",dateIssued.toString()));
				
				Date dateExpires = new Date(cert.getNotAfter());
				form.append(new StringItem("Cert Expires",dateExpires.toString()));
				
				
			}
			catch (IOException ioe)
			{
				Logger.error(TAG, "error access cert info", ioe);
	
				showAlert("Error","Unable to read SSL info: " + ioe.getMessage(),this.form);
			}
		}
		else
		{
			//form.append(new StringItem("Error","Unable to read SSL certificate information"));

		}

	}

	public void downloadError(Exception e) {
		
		if (e != null)
		{
			
			//showAlert("Error","unable to access url: " + e.getMessage(),this.form);
			form.append(new StringItem("Error","unable to access url: " + e.getMessage()));

			Logger.error(TAG, "error downloading", e);

		}
	}

	public void downloadStatus(int percent) {
		this.form.setTitle("Download " + percent + " %...");
	}

}
