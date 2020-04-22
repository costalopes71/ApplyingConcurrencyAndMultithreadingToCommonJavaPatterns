package com.costalopes;

import com.costalopes.task.TaskA;

public class DeadLockExample {

	public static void main(String[] args) throws InterruptedException {
		
		TaskA task = new TaskA();
		
		Runnable runnable1 = () -> {
			task.a();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		Runnable runnable2 = () -> task.b();
		
		Thread t1 = new Thread(runnable1);
		t1.start();
		
		Thread t2 = new Thread(runnable2);
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println("TERMINATED");
		
	}
	
}
