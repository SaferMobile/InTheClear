/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.net;

public interface DownloadListener {

	/**
	 * Called when download is completed.
	 * 
	 * @param data downloaded data.
	 */
	public void downloadCompleted(byte[] data);

	/**
	 * Called to update download status.
	 * 
	 * @param percent percent downloaded.
	 */
	public void downloadStatus(int percent);
	
	/**
	 * Called upon download error.
	 * 
	 * @param e error.
	 */
	public void downloadError(Exception e);
}
