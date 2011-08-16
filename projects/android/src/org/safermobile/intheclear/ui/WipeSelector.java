package org.safermobile.intheclear.ui;

import java.io.File;

public class WipeSelector {
	String _wipeTarget;
	int _wipeType;
	boolean _wipeSelect;
	boolean _isToggleControl = false;
	int _color = Color.UNSELECTABLE;
	File _path;
	
	public static class Color {
		public static final int SELECTABLE = android.graphics.Color.BLACK;
		public static final int UNSELECTABLE = android.graphics.Color.LTGRAY;
	}
	
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
	
	public int getColor() {
		return _color;
	}
	
	public boolean isToggleControl() {
		return _isToggleControl;
	}
	
	public void setIsToggleControl(boolean b) {
		_isToggleControl = b;
	}
	
	public void setColor(int which) {
		_color = which;
	}
}
