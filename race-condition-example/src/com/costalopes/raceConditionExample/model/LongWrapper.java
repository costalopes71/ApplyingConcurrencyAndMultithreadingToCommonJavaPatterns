package com.costalopes.raceConditionExample.model;

public class LongWrapper {

	private long value;

	public LongWrapper(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}
	
	public void incrementValue() {
		value = value + 1;
	}
	
}
