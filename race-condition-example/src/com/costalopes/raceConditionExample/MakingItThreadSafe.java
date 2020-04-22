package com.costalopes.raceConditionExample;

import com.costalopes.raceConditionExample.model.ThreadSafeLongWrapper;

public class MakingItThreadSafe {

	public static void main(String[] args) throws InterruptedException {

		ThreadSafeLongWrapper longWrapper = new ThreadSafeLongWrapper(0L);

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
