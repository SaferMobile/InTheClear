package org.safermobile.clear.micro.apps.controllers;

public interface WipeListener {

	public void wipingFileSuccess (String path);

	public void wipingFileError (String path, String err);

	public void wipePercentComplete (int percent);
}
