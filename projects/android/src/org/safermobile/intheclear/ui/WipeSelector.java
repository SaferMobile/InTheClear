package org.safermobile.intheclear.ui;

import java.io.File;

public class WipeSelector {
	String _wipeTarget;
	int _wipeType;
	boolean _wipeSelect;
	File _path;
	
	public WipeSelector(String wipeTarget, int wipeType, boolean wipeSelect) {
		_wipeTarget = wipeTarget;
		_wipeType = wipeType;
		_wipeSelect = wipeSelect;
	}
	
	public int getWipeType() {
		return _wipeType;
	}
	
	public void setSelected(boolean select) {
		_wipeSelect = select;
	}
	
	public boolean getSelected() {
		return _wipeSelect;
	}
	
	public void setFilePath(File path) {
		_path = path;
	}
	
	public File getFilePath() {
		return _path;
	}
}
