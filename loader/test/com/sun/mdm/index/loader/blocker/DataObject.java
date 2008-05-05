package com.sun.mdm.index.loader.blocker;

public class DataObject {
	private String value;
	
	public DataObject() {
	}
	
	public DataObject(String value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean equals(DataObject data) {
	
		if (value == null) {
			if (data.getValue() == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return value.equals(data.getValue()); 
		}
	}
}
