package com.costalopes.runnable;

public class FirstRunnable {

	public static void main(String[] args) {
		
		Runnable runnable = () -> {
			System.out.println("I am running in " + Thread.currentThread().getName());
		};
		
		Thread thread = new Thread(runnable);
		thread.setName("My Thread");
		
		thread.start();
		
		// Nao devo chamar esse metodo se quero rodar a tarefa na thread criada, pois o metodo run ira executar na thread main.
		// thread.run();
		
	}
	
}
