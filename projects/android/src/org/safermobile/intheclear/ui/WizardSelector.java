package org.safermobile.intheclear.ui;

public class WizardSelector {
	String _listItemText;
	int _listItemNum;
	boolean _hasBeenTested;
	
	public WizardSelector(String listItemText,int listItemNum) {
		_listItemText = listItemText;
		_listItemNum = listItemNum + 1;
		_hasBeenTested = false;
	}
	
	public void setTested(boolean tested) {
		_hasBeenTested = tested;
	}
	
	public boolean getTested() {
		return _hasBeenTested;
	}
}
