package org.safermobile.clear.micro.apps.models;

public class WipeDataType {

	private String key;
	private String label;
	private boolean enabled;
	
	public WipeDataType (String key, String label)
	{
		this.key = key;
		this.label = label;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
