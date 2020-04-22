package com.costalopes.task;

public class TaskA {

	private Object key1 = new Object();
	private Object key2 = new Object();
	
	public void a() {
		synchronized (key1) {
			System.out.println("[" + Thread.currentThread().getName() + "] Im in a()");
			b();
		}
	}

	public void b() {
		synchronized (key2) {
			System.out.println("[" + Thread.currentThread().getName() + "] Im in b()");
			c();
		}
	}

	public void c() {
		synchronized (key1) {
			System.out.println("[" + Thread.currentThread().getName() + "] Im in c()");
		}
	}
	
}
