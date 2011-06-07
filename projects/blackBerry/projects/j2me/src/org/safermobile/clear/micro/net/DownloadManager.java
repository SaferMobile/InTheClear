/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */
package org.safermobile.clear.micro.net;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;


public class DownloadManager implements Runnable {

	private Vector listeners = new Vector();
	private boolean running;
	private String  url;
	
	private HttpsConnection connection  = null;
	
	public void download(String url) {
		if (!running) {
			this.url = url;
			Thread t = new Thread(this);
			t.start();
		}
	}
	
	public HttpsConnection getConnection ()
	{
		return connection;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		ByteArrayOutputStream buffer = null;
		InputStream    inputStream = null;
		Enumeration    enumeration = null;
		
		running = true;
		try {
			buffer = new ByteArrayOutputStream();
			
			connection  = (HttpsConnection) Connector.open(this.url);
			inputStream = connection.openInputStream();
			
			long length = connection.getLength();
			byte[] data = new byte[512];
			int total = 0x00;
			int read  = -1;
			do {
				read = inputStream.read(data);
				if (read > 0x00) {
					total += read;
					buffer.write(data, 0x00, read);
					enumeration = this.listeners.elements();
					while (enumeration.hasMoreElements()) {
						DownloadListener listener = (DownloadListener) enumeration.nextElement();
						listener.downloadStatus((int)(total * 100 / length));
					}
				}
			} while (read != -1);
		} catch (Exception e) {
			enumeration = this.listeners.elements();
			while (enumeration.hasMoreElements()) {
				DownloadListener listener = (DownloadListener) enumeration.nextElement();
				listener.downloadError(e);
			}
		} finally {
			enumeration = this.listeners.elements();
			while (enumeration.hasMoreElements()) {
				DownloadListener listener = (DownloadListener) enumeration.nextElement();
				listener.downloadCompleted(buffer.toByteArray());
			}
			
			if (inputStream != null)
			{
				try {				
					inputStream.close();
					connection.close();
				} catch (Exception e) {
					enumeration = this.listeners.elements();
					while (enumeration.hasMoreElements()) {
						DownloadListener listener = (DownloadListener) enumeration.nextElement();
						listener.downloadError(e);
					}
				}
			}
		}
		running = false;
	}

	/**
	 * Adds a listener for downloads.
	 * 
	 * @param listener target listener.
	 */
	public void addListener(DownloadListener listener) {
		if (!this.listeners.contains(listener)) {			
			this.listeners.addElement(listener);
		}
	}
	
	/**
	 * Removes a listener for downloads.
	 * 
	 * @param listener target listener.
	 */
	public void removeListener(DownloadListener listener) {
		this.listeners.removeElement(listener);
	}
}
