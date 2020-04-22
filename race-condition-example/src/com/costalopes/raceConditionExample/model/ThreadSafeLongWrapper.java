package com.costalopes.raceConditionExample.model;

public class ThreadSafeLongWrapper {

	private long value;
	private Object key = new Object();

	public ThreadSafeLongWrapper(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void incrementValue() {
		synchronized (key) {
			value = value + 1;
		}
	}

}
