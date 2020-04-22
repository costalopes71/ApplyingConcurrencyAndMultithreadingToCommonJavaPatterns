package com.costalopes.raceConditionExample;

import com.costalopes.raceConditionExample.model.LongWrapper;

public class RaceCondition {

	public static void main(String[] args) throws InterruptedException {
		
		LongWrapper longWrapper = new LongWrapper(0L);
		
		Runnable task = () -> {
			for (int i = 0; i < 1_000; i++) {
				longWrapper.incrementValue();
			}
		};
		
		Thread[] threads = new Thread[1000];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(task);
			threads[i].start();
		}
		
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		
		System.out.println("Value = " + longWrapper.getValue());
		
	}
	
}
